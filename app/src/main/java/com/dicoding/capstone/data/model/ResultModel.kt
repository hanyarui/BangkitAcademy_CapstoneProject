package com.dicoding.capstone.data.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


data class ResultModel(
    val name: String,
    val status: String,
    val photoUrl: String
) {
    fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val filesDir = context.filesDir
        val imageFile = File(filesDir, "${System.currentTimeMillis()}.png")
        val os: FileOutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(imageFile)
    }

    fun Bitmap.toUriString(context: Context): String {
        val uri = saveBitmapToFile(context, this)
        return uri.toString()
    }
}
