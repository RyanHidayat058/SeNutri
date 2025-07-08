package com.example.covary.retrofit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MenuResponse(val Menu: List<MenuItem>)

@Parcelize
data class MenuItem(
    val nama: String,
    val gram: String,
    val kalori: Double,
    val protein: Double,
    val lemak: Double,
    val karbohidrat: Double
) : Parcelable
