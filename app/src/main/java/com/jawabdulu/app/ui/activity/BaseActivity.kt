package com.jawabdulu.app.ui.activity

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {


    fun createToast(content: String){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }
}