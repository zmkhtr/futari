package com.jawabdulu.app.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jawabdulu.app.R
import kotlinx.coroutines.*


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        skipLayout()
    }

    private fun skipLayout(){

        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            startActivity(Intent(this@SplashScreenActivity, PasscodeActivity::class.java))
            finish()
        }

    }
}