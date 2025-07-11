package com.example.covary.imgurapi

data class ImgurResponse(
    val data: ImgurData,
    val success: Boolean,
    val status: Int
)

data class ImgurData(
    val id: String,
    val link: String
)
