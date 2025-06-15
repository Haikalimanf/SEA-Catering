package com.example.seacatering.utils

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun copyImageToInternalStorage(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = File(context.cacheDir,"image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(fileName)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            fileName.toUri()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}