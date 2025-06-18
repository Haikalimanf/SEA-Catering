package com.example.seacatering.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataTestimonial(
    val id: String,
    val userId: String,
    val reviewText: String,
    val rating: Int,
    val date: Timestamp
) : Parcelable
