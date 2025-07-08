package com.example.covary.retrofit

import retrofit2.http.GET

interface ApiService {
    @GET("ivanfdillah/data-makanan/main/Makanan.json")
    suspend fun getMenu(): MenuResponse
}