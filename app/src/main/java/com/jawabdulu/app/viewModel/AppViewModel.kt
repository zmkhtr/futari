package com.jawabdulu.app.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jawabdulu.app.MyApplication
import com.jawabdulu.app.models.AppModel
import com.jawabdulu.app.models.QuizResponse
import com.jawabdulu.app.repository.AppRepository
import com.jawabdulu.app.repository.QuizRepository
import com.jawabdulu.app.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AppViewModel(
    app : Application,
    val appRepository : AppRepository
) : AndroidViewModel(app) {

    val app : MutableLiveData<Resource<AppModel>> = MutableLiveData()

    fun saveApp(appModel: AppModel) = viewModelScope.launch {
        appRepository.upsert(appModel)
    }

    fun getAllApp() = appRepository.getAllApp()

//    fun getAllLockedApp() = appRepository.getAllLockedApp(true)

    fun deleteApp(appModel: AppModel) = viewModelScope.launch {
        appRepository.deleteApp(appModel)
    }



}
