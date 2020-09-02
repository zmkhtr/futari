package com.jawabdulu.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jawabdulu.app.R
import com.jawabdulu.app.preferences.Preferences
import kotlinx.coroutines.*


class SplashScreenActivity : AppCompatActivity() {
    private val TAG = "SplashScreenActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        skipLayout()
    }

    private fun skipLayout(){

        Log.d(TAG, "skipLayout: " +  Preferences.getDataAnak())
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            when {
                Preferences.isFirstOpenSlider() -> {
                    startActivity(Intent(this@SplashScreenActivity, IntroSliderActivity::class.java))
                    finish()
                }
                Preferences.getDataAnak() == null -> {
                    startActivity(Intent(this@SplashScreenActivity, DataAnakActivity::class.java))
                    finish()
                }
                Preferences.getDataOrangTua() == null -> {
                    startActivity(Intent(this@SplashScreenActivity, DataOrangTuaActivity::class.java))
                    finish()
                }
                !Preferences.getDataOrangTua()!!.verified -> {
                    startActivity(Intent(this@SplashScreenActivity, DataOrangTuaActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(this@SplashScreenActivity, PasscodeActivity::class.java))
                    finish()
                }
            }

        }

    }
}