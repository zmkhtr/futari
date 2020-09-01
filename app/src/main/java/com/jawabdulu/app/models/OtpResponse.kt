package com.jawabdulu.app.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class OtpResponse {
    @SerializedName("msgId")
    @Expose
    var msgId: String? = null

    @SerializedName("status")
    @Expose
    var isStatus = false

    @SerializedName("maxAttempt")
    @Expose
    var maxAttempt: String? = null

    @SerializedName("expireIn")
    @Expose
    var expireIn = 0

    @SerializedName("message")
    @Expose
    var message: String? = null
}