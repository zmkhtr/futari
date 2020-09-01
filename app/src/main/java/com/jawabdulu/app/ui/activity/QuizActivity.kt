package com.jawabdulu.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jawabdulu.app.models.QuizResponse
import com.jawabdulu.app.R
import com.jawabdulu.app.api.ApiInterface
import kotlinx.android.synthetic.main.activity_quiz.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizActivity : AppCompatActivity() {

    lateinit var listQuiz : QuizResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)





    }
}