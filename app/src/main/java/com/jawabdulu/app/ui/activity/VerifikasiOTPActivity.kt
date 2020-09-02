package com.jawabdulu.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jawabdulu.app.R
import com.jawabdulu.app.preferences.Preferences
import com.jawabdulu.app.repository.BigBoxRepository
import com.jawabdulu.app.util.Resource
import com.jawabdulu.app.viewModel.BigBoxApiViewModel
import com.jawabdulu.app.viewModel.BigBoxViewModelFactory
import kotlinx.android.synthetic.main.activity_verifikasi_o_t_p.*
import java.util.concurrent.TimeUnit


class VerifikasiOTPActivity : BaseActivity() {
    private val TAG = "VerifikasiOTPActivity"

    lateinit var viewModel : BigBoxApiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_o_t_p)


        val bigBoxRepository = BigBoxRepository()
        val viewModelProviderFactory = BigBoxViewModelFactory(application, bigBoxRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(BigBoxApiViewModel::class.java)


        viewModel.getOTP(Preferences.getDataOrangTua()!!.nomorHandphone)

        countDownTimer()
        otp_view.requestFocus()
        window.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
        setOTPHandler()

        val noHP = Preferences.getDataOrangTua()!!.nomorHandphone
        tvOTPDescription.text = "Silahkan check kode verifikasi OTP Dikirim ke " + noHP
    }


    private fun countDownTimer(){
        tvOTPCountDown.isEnabled = false
        object : CountDownTimer(60000, 1000) {
            // adjust the milli seconds here
            override fun onTick(millisUntilFinished: Long) {
                val min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val sec = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                tvOTPCountDown.text = "Kirim ulang kode dalam  $min:$sec"
            }

            override fun onFinish() {
                enableResendOTPButton()
            }
        }.start()
    }

    private fun enableResendOTPButton(){
        tvOTPCountDown.text = "Kirim ulang!"
        tvOTPCountDown.isEnabled = true

        tvOTPCountDown.setOnClickListener {
            viewModel.getOTP(Preferences.getDataOrangTua()!!.nomorHandphone)
            viewModel.otp.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        progressBarOTP.visibility = View.INVISIBLE
                        response.data?.let { otpresponse ->
                            Log.d(TAG, "OTPHandler: $otpresponse")
                            Toast.makeText(this, "Berhasil Kirim Ulang OTP", Toast.LENGTH_SHORT)
                                .show()
                            countDownTimer()
                        }
                    }
                    is Resource.Error -> {
                        progressBarOTP.visibility = View.INVISIBLE
                        response.message?.let { message ->
                            Log.e(TAG, "An error occured $message")
                            Toast.makeText(this, "Error Kirim ulang OTP : $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Loading -> {
                        progressBarOTP.visibility = View.VISIBLE
                    }
                }
            })
        }

    }
    private fun setOTPHandler(){
        otp_view.setOtpCompletionListener {
            viewModel.verificationOtp(it)
            viewModel.otpVerification.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        progressBarOTP.visibility = View.INVISIBLE
                        response.data?.let { otpresponse ->
                            Log.d(TAG, "OTPHandler: $otpresponse")
                            Toast.makeText(this, "Berhasil Verifikasi OTP", Toast.LENGTH_SHORT)
                                .show()
                            val ortu = Preferences.getDataOrangTua()
                            ortu?.verified = true
                            Preferences.setDataOrangTua(ortu!!)
                            startActivity(Intent(this, PasscodeActivity::class.java))
                            finish()
                        }
                    }
                    is Resource.Error -> {
                        progressBarOTP.visibility = View.INVISIBLE
                        response.message?.let { message ->
                            Log.e(TAG, "An error occured $message")
                            Toast.makeText(this, "Error OTP : $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Loading -> {
                        progressBarOTP.visibility = View.VISIBLE
                    }
                }
            })
        }
    }
}