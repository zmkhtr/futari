package com.jawabdulu.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jawabdulu.app.models.AppModel
import com.jawabdulu.app.models.QuizResponse

@Database(
    entities = [QuizResponse.QuizResponseItem::class, AppModel::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun getQuizDao() : QuizDao

    companion object {
        @Volatile
        private var instance : QuizDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                QuizDatabase::class.java,
                "article_db.db"
            ).build()
    }
}