package com.jawabdulu.app.repository

import com.jawabdulu.app.api.RetrofitInstance
import com.jawabdulu.app.db.QuizDatabase
import com.jawabdulu.app.models.QuizResponse

class QuizRepository(
    val db : QuizDatabase
) {

    suspend fun getAllQuiz() = RetrofitInstance.api.getAllQuiz()

    suspend fun upsert(quiz : QuizResponse.QuizResponseItem) = db.getQuizDao().upsert(quiz)

    fun getListQuiz() = db.getQuizDao().getAllQuiz()

    suspend fun deleteQuiz(quiz: QuizResponse.QuizResponseItem) = db.getQuizDao().deleteQuiz(quiz)

    fun getQuiz(quizID: Int, kelas : String) = db.getQuizDao().getQuiz(quizID, kelas)
}