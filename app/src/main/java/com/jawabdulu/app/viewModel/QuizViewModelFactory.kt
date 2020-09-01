package com.jawabdulu.app.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jawabdulu.app.repository.QuizRepository

class QuizViewModelFactory (
    val app : Application,
    val quizRepository: QuizRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuizViewModel(app, quizRepository) as T
    }

}