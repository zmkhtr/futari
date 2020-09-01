package com.jawabdulu.app.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class OtpVerificationResponse {
    @SerializedName("maxAttempt")
    @Expose
    var maxAttempt = 0

    @SerializedName("expireIn")
    @Expose
    var expireIn: String? = null

    @SerializedName("status")
    @Expose
    var isStatus = false

    @SerializedName("message")
    @Expose
    var message: String? = null
}