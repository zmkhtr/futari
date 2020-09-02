package com.jawabdulu.app.repository

import com.jawabdulu.app.api.RetrofitInstance
import com.jawabdulu.app.db.QuizDatabase
import com.jawabdulu.app.models.AppModel
import com.jawabdulu.app.models.QuizResponse

class AppRepository(
    val db : QuizDatabase
) {


    suspend fun upsert(app : AppModel) = db.getQuizDao().upsertApp(app)

    fun getAllApp() = db.getQuizDao().getAllApp()

//    fun getAllLockedApp(locked: Boolean) = db.getQuizDao().getAllLockedApp(locked)

    suspend fun deleteApp(appModel: AppModel) = db.getQuizDao().deleteApp(appModel)

//    fun getApp(quizID: Int, kelas : String) = db.getQuizDao().getQuiz(quizID, kelas)
}