package com.jawabdulu.app.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class QuizResponse : ArrayList<QuizResponse.QuizResponseItem>(){
    @Entity(
        tableName = "quizzes"
    )
    data class QuizResponseItem(
        @SerializedName("answer")
        var answer: String? = null,
        @SerializedName("grade")
        var grade: String? = null,
        @SerializedName("grade_level")
        var gradeLevel: String? = null,
        @SerializedName("id")
        @PrimaryKey
        var id: Int? = null,
        @SerializedName("option_four")
        var optionFour: String? = null,
        @SerializedName("option_one")
        var optionOne: String? = null,
        @SerializedName("option_three")
        var optionThree: String? = null,
        @SerializedName("option_two")
        var optionTwo: String? = null,
        @SerializedName("question")
        var question: String? = null
    )
}