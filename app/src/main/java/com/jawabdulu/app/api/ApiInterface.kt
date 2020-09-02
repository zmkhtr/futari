package com.jawabdulu.app.api

import com.jawabdulu.app.models.MessagesResponse
import com.jawabdulu.app.models.OtpResponse
import com.jawabdulu.app.models.OtpVerificationResponse
import com.jawabdulu.app.models.QuizResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    @GET("quiz/")
    suspend fun getAllQuiz() : Response<QuizResponse>

    @FormUrlEncoded
    @Headers("x-api-key: jGuKSph0czODvEbNxGqPMoOWrycdiVDW")
    @PUT("https://api.thebigbox.id/sms-otp/1.0.0/otp/fxaAFKMDsnasgf9N0gHQD29Z4NMxljzq")
    suspend fun getOtp(
        @Field("maxAttempt") attempt: String?,  //3
        @Field("phoneNum") phoneNumber : String?,  //+62
        @Field("expireIn") expire: String?,  //60 second
        @Field("content") content: String?,  //{{otp}}
        @Field("digit") digit: String?
    ): Response<OtpResponse> //4


    @FormUrlEncoded
    @Headers("x-api-key: jGuKSph0czODvEbNxGqPMoOWrycdiVDW")
    @POST("https://api.thebigbox.id/sms-otp/1.0.0/otp/fxaAFKMDsnasgf9N0gHQD29Z4NMxljzq/verifications")
    suspend fun verificationOtp(
        @Field("maxAttempt") attempt: String?,  //3
        @Field("expireIn") expire: String?,  //60 second
        @Field("otpstr") otpstr: String?,  //{{otp}}
        @Field("digit") digit: String?
    ): Response<OtpVerificationResponse> //4

    @FormUrlEncoded
    @Headers("x-api-key: jGuKSph0czODvEbNxGqPMoOWrycdiVDW")
    @PUT("https://api.thebigbox.id/sms-premium/1.0.0/messages")
    suspend fun sendSMSCustom(
        @Field("msisdn") phoneNumber : String?,  //3
        @Field("content") contentSMS : String?
    ): Response<MessagesResponse> //4


}