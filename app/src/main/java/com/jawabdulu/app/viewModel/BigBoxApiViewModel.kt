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
import com.jawabdulu.app.models.MessagesResponse
import com.jawabdulu.app.models.OtpResponse
import com.jawabdulu.app.models.OtpVerificationResponse
import com.jawabdulu.app.repository.BigBoxRepository
import com.jawabdulu.app.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class BigBoxApiViewModel(
    app : Application,
    val bigBoxRepository: BigBoxRepository
) : AndroidViewModel(app) {

    val otp : MutableLiveData<Resource<OtpResponse>> = MutableLiveData()
    val otpVerification : MutableLiveData<Resource<OtpVerificationResponse>> = MutableLiveData()

    val sms : MutableLiveData<Resource<MessagesResponse>> = MutableLiveData()

    fun getOTP(phoneNumber : String) = viewModelScope.launch {
        safeOtpCall(phoneNumber)
    }

    private fun handleOtpResponse(response: Response<OtpResponse>): Resource<OtpResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    private suspend fun safeOtpCall(phoneNumber : String) {
        otp.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = bigBoxRepository.getOtp(phoneNumber)
                otp.postValue(handleOtpResponse(response))
            } else {
                otp.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t){
                is IOException -> otp.postValue(Resource.Error("Network Failure"))
                else -> otp.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun verificationOtp(otp : String) = viewModelScope.launch {
        safeOtpVerificationCall(otp)
    }

    private fun handleOtpVerificationResponse(response: Response<OtpVerificationResponse>): Resource<OtpVerificationResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    private suspend fun safeOtpVerificationCall(otp : String) {
        otpVerification.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = bigBoxRepository.getOtpVerification(otp)
                otpVerification.postValue(handleOtpVerificationResponse(response))
            } else {
                otpVerification.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t){
                is IOException -> otpVerification.postValue(Resource.Error("Network Failure"))
                else -> otpVerification.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun sendSMS(phoneNumber : String, content : String) = viewModelScope.launch {
        safeSendSMSCall(phoneNumber, content)
    }

    private fun handleSendSMSResponse(response: Response<MessagesResponse>): Resource<MessagesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    private suspend fun safeSendSMSCall(phoneNumber : String, content : String) {
        sms.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = bigBoxRepository.sendSMSCustom(phoneNumber, content)
                sms.postValue(handleSendSMSResponse(response))
            } else {
                sms.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t){
                is IOException -> sms.postValue(Resource.Error("Network Failure"))
                else -> sms.postValue(Resource.Error("Conversion Error $t"))
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
}