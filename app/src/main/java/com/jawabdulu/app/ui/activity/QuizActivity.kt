package com.jawabdulu.app.ui.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jawabdulu.app.models.QuizResponse
import com.jawabdulu.app.R
import com.jawabdulu.app.api.ApiInterface
import com.jawabdulu.app.db.QuizDatabase
import com.jawabdulu.app.preferences.Preferences
import com.jawabdulu.app.repository.QuizRepository
import com.jawabdulu.app.viewModel.QuizViewModel
import com.jawabdulu.app.viewModel.QuizViewModelFactory
import kotlinx.android.synthetic.main.activity_quiz.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizActivity : BaseActivity() {
    private val TAG = "QuizActivity"

    lateinit var viewModel : QuizViewModel
    var random : Int = 0
    lateinit var listQuiz : List<QuizResponse.QuizResponseItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val quizRepository = QuizRepository(QuizDatabase(this))
        val viewModelProviderFactory = QuizViewModelFactory(application, quizRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(QuizViewModel::class.java)

        viewModel.getAllQuiz().observe(this, Observer {

            listQuiz = it
            randomQuiz()
        })
    }

    private fun randomQuiz(){
        val ff = Preferences.getDataAnak()

        Log.d(TAG, "randomQuiz: 1 ${listQuiz.size}")

        listQuiz = listQuiz.filter {
            it.gradeLevel!!.contains(Preferences.getDataAnak()!!.kelas)
        }
        random = (listQuiz.indices-1).random()

        Log.d(TAG, "randomQuiz: $ff")
        Log.d(TAG, "randomQuiz: $random")
        Log.d(TAG, "randomQuiz: 2 ${listQuiz.size}")
        viewModel.getQuizRandom(listQuiz[random].id!!, Preferences.getDataAnak()!!.kelas).observe(this, Observer {
            Log.d(TAG, "onCreate:2 $it")
            setQuiz(it[0])
            quizHandler(it[0])
        })
    }

    private fun setQuiz(quizResponseItem: QuizResponse.QuizResponseItem){
        tvQuizSubjectAndClass.text = "Matematika " + quizResponseItem.gradeLevel
        tvAnswerA.text = quizResponseItem.optionOne
        tvAnswerB.text = quizResponseItem.optionTwo
        tvAnswerC.text = quizResponseItem.optionThree
        tvAnswerD.text = quizResponseItem.optionFour
        tvQuizQuestion.text = quizResponseItem.question
    }

    private fun rightAnswer(){
        val dataAnak = Preferences.getDataAnak()
        dataAnak?.totalBenar = dataAnak?.totalBenar?.plus(1)!!
        Preferences.setDataAnak(dataAnak)

        Preferences.setWrongCount(0)
        Log.d(TAG, "rightAnswer: " + Preferences.getDataAnak())
        val intent = Intent(this, WinActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun wrongAnswer(){
        val dataAnak = Preferences.getDataAnak()
        dataAnak?.totalSalah = dataAnak?.totalSalah?.plus(1)!!
        Preferences.setDataAnak(dataAnak)


        val wrong = Preferences.getWrongCount() + 1
        Preferences.setWrongCount(wrong)

        Log.d(TAG, "wrongAnswer: " + Preferences.getDataAnak())
        Log.d(TAG, "wrongAnswer:aa ${wrong % 3 == 0}")
        if (Preferences.getWrongCount() % 3 == 0) {
            val intent = Intent(this, LockedActivity::class.java)
            intent.putExtra("COUNTDOWN_TIME", Preferences.getWrongCount() / 3 * 60000L)
            startActivity(intent)
            finishAffinity()
        } else {
            randomQuiz()
        }
    }

    private fun quizHandler(quizResponseItem: QuizResponse.QuizResponseItem) {
        cvQuizAnswerA.setOnClickListener {
            if (quizResponseItem.optionOne == quizResponseItem.answer) {
                rightAnswer()
            } else {
                createToast("Jawabanmu Salah :(")
                wrongAnswer()
            }
        }

        cvQuizAnswerB.setOnClickListener {
            if (quizResponseItem.optionTwo == quizResponseItem.answer) {
                rightAnswer()
            } else {
                createToast("Jawabanmu Salah :(")
                wrongAnswer()
            }
        }

        cvQuizAnswerC.setOnClickListener {
            if (quizResponseItem.optionThree == quizResponseItem.answer) {
                rightAnswer()
            } else {
                createToast("Jawabanmu Salah :(")
                wrongAnswer()
            }
        }

        cvQuizAnswerD.setOnClickListener {
            if (quizResponseItem.optionFour == quizResponseItem.answer) {
                rightAnswer()
            } else {
                createToast("Jawabanmu Salah :(")
                wrongAnswer()
            }
        }


    }

    override fun onBackPressed() {
        //do nothing
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}