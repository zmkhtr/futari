package com.jawabdulu.app.models


import com.google.gson.annotations.SerializedName

data class MessagesResponse(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("msgid")
    var msgid: String? = null,
    @SerializedName("status")
    var status: String? = null
)