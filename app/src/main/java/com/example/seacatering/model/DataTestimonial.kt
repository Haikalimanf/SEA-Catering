package com.example.seacatering.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataTestimonial(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val reviewText: String = "",
    val rating: Int = 0,
    @field:JvmField
    @ServerTimestamp val date: Timestamp? = null
) : Parcelable
