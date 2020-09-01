package com.jawabdulu.app.db

import androidx.room.TypeConverter
import com.jawabdulu.app.models.QuizResponse

class Converters {

    @TypeConverter
    fun fromSource(source : QuizResponse.QuizResponseItem) : String? {
        return source.question
    }

    @TypeConverter
    fun toSource(name : String) : QuizResponse.QuizResponseItem {
        return QuizResponse.QuizResponseItem()
    }
}