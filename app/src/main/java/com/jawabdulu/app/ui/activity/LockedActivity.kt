package com.jawabdulu.app.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.jawabdulu.app.R
import com.jawabdulu.app.preferences.Preferences
import kotlinx.android.synthetic.main.activity_locked.*
import kotlinx.android.synthetic.main.activity_verifikasi_o_t_p.*
import kotlinx.android.synthetic.main.activity_verifikasi_o_t_p.tvOTPCountDown
import java.util.*
import java.util.concurrent.TimeUnit

class LockedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locked)

        countDownTimer()
    }

    private fun countDownTimer(){
        val time = intent.getLongExtra("COUNTDOWN_TIME",0L)
        object : CountDownTimer(time, 1000) {
            // adjust the milli seconds here
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000).toInt() / 60
                val seconds = (millisUntilFinished / 1000).toInt() % 60

                val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                tvLockedCountDown.text = "00:$timeLeftFormatted"
            }

            override fun onFinish() {
                val intent = Intent(this@LockedActivity, QuizActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }.start()
    }

    override fun onBackPressed() {
        //do nothing
    }
}