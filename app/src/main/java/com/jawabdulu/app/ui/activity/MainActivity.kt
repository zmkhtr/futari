package com.jawabdulu.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jawabdulu.app.R
import com.jawabdulu.app.db.QuizDatabase
import com.jawabdulu.app.repository.QuizRepository
import com.jawabdulu.app.viewModel.QuizViewModel
import com.jawabdulu.app.viewModel.QuizViewModelFactory
import com.jawabdulu.app.util.Resource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var viewModel : QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        packageManager = skdjka as android
//

        val quizRepository = QuizRepository(QuizDatabase(this))
        val viewModelProviderFactory = QuizViewModelFactory(application, quizRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(QuizViewModel::class.java)

        viewModel.quiz.observe(this, Observer {response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {quizResponse ->
                        for (quiz in quizResponse){
                            viewModel.saveQuiz(quiz)
                        }
                    }
                }
                is Resource.Error -> {
//                    hideProgressBar()
                    response.message?.let {message ->
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

}