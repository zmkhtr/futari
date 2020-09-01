package com.jawabdulu.app.db

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.room.*
import com.jawabdulu.app.models.AppModel
import com.jawabdulu.app.models.QuizResponse

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(quiz: QuizResponse.QuizResponseItem) : Long

    @Delete
    suspend fun deleteQuiz(quiz: QuizResponse.QuizResponseItem)

    @Query("SELECT * FROM quizzes")
    fun getAllQuiz() : LiveData<List<QuizResponse.QuizResponseItem>>


    @Query("SELECT * FROM quizzes WHERE id = :quizID")
    fun getQuiz(quizID : Int) : LiveData<List<QuizResponse.QuizResponseItem>>




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertApp(appModel: AppModel) : Long

    @Delete
    suspend fun deleteApp(appModel: AppModel)

    @Query("SELECT * FROM apps")
    fun getAllApp() : LiveData<List<QuizResponse.QuizResponseItem>>

    @Query("SELECT * FROM apps WHERE iconAplikasi = :locked")
    fun getAllLockedApp(locked : Drawable) : LiveData<List<QuizResponse.QuizResponseItem>>

}