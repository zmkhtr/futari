package com.jawabdulu.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import kotlinx.android.synthetic.main.activity_verifikasi_o_t_p.*

class VerifikasiOTPActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_o_t_p)

        otp_view.requestFocus()
        window.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)

    }
}