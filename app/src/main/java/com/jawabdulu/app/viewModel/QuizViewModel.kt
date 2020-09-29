package com.jawabdulu.app.viewModel

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jawabdulu.app.MyApplication
import com.jawabdulu.app.models.App
import com.jawabdulu.app.models.QuizResponse
import com.jawabdulu.app.repository.QuizRepository
import com.jawabdulu.app.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class QuizViewModel(
    app : Application,
    val quizRepository : QuizRepository
) : AndroidViewModel(app) {

    private val TAG = "QuizViewModel"

    val quiz : MutableLiveData<Resource<QuizResponse>> = MutableLiveData()
    val apps : MutableLiveData<List<PackageInfo>> = MutableLiveData()

    init {
        getInstalledApps(app)
    }

    fun getQuizNetwork() = viewModelScope.launch {
        safeQuizCall()
    }

    fun saveQuiz(quiz : QuizResponse.QuizResponseItem) = viewModelScope.launch {
        quizRepository.upsert(quiz)
    }

    fun getAllQuiz() = quizRepository.getListQuiz()

    fun deleteQuiz(quiz: QuizResponse.QuizResponseItem) = viewModelScope.launch {
        quizRepository.deleteQuiz(quiz)
    }

    fun getQuizRandom(quizID : Int, kelas : String) = quizRepository.getQuiz(quizID, kelas)

    private fun handleQuizResponse(response: Response<QuizResponse>): Resource<QuizResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    private suspend fun safeQuizCall() {
        quiz.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = quizRepository.getAllQuiz()
                quiz.postValue(handleQuizResponse(response))
            } else {
                quiz.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t){
                is IOException -> quiz.postValue(Resource.Error("Network Failure"))
                else -> quiz.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return  false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun getInstalledApps(context: Context){
        val packs = context.packageManager.getInstalledPackages(0)

        apps.postValue(packs)
    }
}
