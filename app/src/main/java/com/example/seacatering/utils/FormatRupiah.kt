package com.example.seacatering.utils

import java.text.NumberFormat
import java.util.Locale

object FormatRupiah {
    fun formatRupiah(amount: Int?): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }
}
