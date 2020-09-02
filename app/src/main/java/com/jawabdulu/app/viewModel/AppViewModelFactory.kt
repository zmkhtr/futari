package com.jawabdulu.app.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jawabdulu.app.repository.AppRepository
import com.jawabdulu.app.repository.QuizRepository

class AppViewModelFactory (
    val app : Application,
    val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppViewModel(app, appRepository) as T
    }

}