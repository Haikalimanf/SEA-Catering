package com.example.seacatering.utils

import android.app.AlertDialog
import android.content.Context

object DialogUtil {
    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveText: String = "Yes",
        negativeText: String = "No",
        onConfirmed: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { _, _ -> onConfirmed() }
            .setNegativeButton(negativeText, null)
            .show()
    }
}