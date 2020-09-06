package com.jawabdulu.app.ui.activity

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jawabdulu.app.R
import com.jawabdulu.app.db.QuizDatabase
import com.jawabdulu.app.repository.AppRepository
import com.jawabdulu.app.repository.BigBoxRepository
import com.jawabdulu.app.repository.QuizRepository
import com.jawabdulu.app.servicejava.BackgroundService
import com.jawabdulu.app.util.Resource
import com.jawabdulu.app.viewModel.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var viewModel : QuizViewModel
    lateinit var viewModelApp : AppViewModel
    lateinit var viewModelBigBox : BigBoxApiViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quizRepository = QuizRepository(QuizDatabase(this))
        val appRepository = AppRepository(QuizDatabase(this))
        val viewModelProviderFactory = QuizViewModelFactory(application, quizRepository)
        val viewModelProviderFactoryApp = AppViewModelFactory(application, appRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(QuizViewModel::class.java)
        viewModelApp = ViewModelProvider(this, viewModelProviderFactoryApp).get(AppViewModel::class.java)



        val bigBoxRepository = BigBoxRepository()
        val viewModelProviderFactoryBigBox = BigBoxViewModelFactory(application, bigBoxRepository)
        viewModelBigBox = ViewModelProvider(this, viewModelProviderFactoryBigBox).get(
            BigBoxApiViewModel::class.java
        )


        viewModel.quiz.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { quizResponse ->
                        for (quiz in quizResponse) {
                            viewModel.saveQuiz(quiz)
                        }
                    }
                }
                is Resource.Error -> {
//                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured $message")
                        Toast.makeText(this, "An error occured $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
//                    showProgressBar()
                }
            }
        })


        toolbar.title = resources.getString(R.string.app_name)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))


        bottomNavigationView.setupWithNavController(mainNavHostFragment.findNavController())

    }


    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}