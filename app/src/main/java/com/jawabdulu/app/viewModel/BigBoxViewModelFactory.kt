package com.jawabdulu.app.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jawabdulu.app.repository.BigBoxRepository

class BigBoxViewModelFactory (
    val app : Application,
    val bigBoxRepository: BigBoxRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BigBoxApiViewModel(app, bigBoxRepository) as T
    }

}