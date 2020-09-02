package com.jawabdulu.app.repository

import com.jawabdulu.app.api.RetrofitInstance

class BigBoxRepository(
) {
//    suspend fun getOTP(countryCode : String, pageNumber : Int) =
//        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
//
//
//    suspend fun searchNews(searchQuery : String, pageNumber: Int) =
//        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
//
//    suspend fun upsert(article : NewsResponse.Article) = db.getArticleDao().upsert(article)
//
//    fun getSavedNews() = db.getArticleDao().getAllArticles()
//
//    suspend fun deleteArticle(article: NewsResponse.Article) = db.getArticleDao().deleteArticle(article)

    suspend fun getOtp(phoneNumber : String) =
        RetrofitInstance.api.getOtp("3", phoneNumber,"60", "Kode OTP Untuk aplikasi Jawab Dulu adalah : {{otp}}","6")

    suspend fun getOtpVerification(otp : String) =
        RetrofitInstance.api.verificationOtp("3","60", otp,"6")

    suspend fun sendSMSCustom(phoneNumber: String, content : String) =
        RetrofitInstance.api.sendSMSCustom(phoneNumber, content)

//    @Field("maxAttempt") attempt: String?,  //3
//    @Field("phoneNum") phoneNumber : String?,  //+62
//    @Field("expireIn") expire: String?,  //60 second
//    @Field("content") content: String?,  //{{otp}}
//    @Field("digit") digit: String?
}