package com.example.seacatering.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataAdvantages(
    val id: String = "",
    val title: String = "",
    val shortDescription: String = "",
    val imageUri: String = "",
) : Parcelable
