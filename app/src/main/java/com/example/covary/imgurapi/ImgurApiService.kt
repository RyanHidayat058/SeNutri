package com.example.covary.imgurapi

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ImgurApiService {
    @Multipart
    @POST("3/image")
    fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part
    ): Call<ImgurResponse>
}