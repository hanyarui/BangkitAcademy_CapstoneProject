package com.dicoding.capstone.util

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.dicoding.capstone.BuildConfig
import com.dicoding.capstone.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Base64

private const val FILENAME_FORMAT = "dd-MM-yyyy"

val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.png")
}

//fun createFile(context: Context): File {
//    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//    val storageDir = context.getExternalFilesDir(null)
//    return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
//}

fun rotateFile (file: File, isBackCamera: Boolean = false) {
    val matrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)
    val rotation = if (isBackCamera) 90f else -90f
    matrix.postRotate(rotation)
    if (!isBackCamera) {
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
    }
    val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    contentResolver.openInputStream(selectedImg)?.use { inputStream ->
        FileOutputStream(myFile).use { outputStream ->
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) {
                outputStream.write(buf, 0, len)
            }
        }
    } ?: throw IllegalArgumentException("Cannot open input stream for URI: $selectedImg")

    return myFile
}

fun uriToFilePredict(uri: Uri, context: Context): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "temp_image.jpg") // Temp file di cache directory
    val outputStream = FileOutputStream(file)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return file
}

fun reduceFileSize(myFile: File): File {
    val maxImageSize = 1000000
    if (myFile.length() > maxImageSize) {
        var streamLength = maxImageSize
        var compressQuality = 105
        val bmpStream = ByteArrayOutputStream()
        while (streamLength >= maxImageSize && compressQuality > 5) {
            bmpStream.use {
                it.flush()
                it.reset()
            }

            compressQuality -= 5
            val bitmap = BitmapFactory.decodeFile(myFile.absolutePath, BitmapFactory.Options())
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            if (BuildConfig.DEBUG) {
                Log.d("test upload", "Quality: $compressQuality")
                Log.d("test upload", "Size: $streamLength")
                Log.e("test upload", "Size: $streamLength")
            }
        }

        FileOutputStream(myFile).use {
            it.write(bmpStream.toByteArray())
        }
    }
    return myFile
}

fun Bitmap.toUriString(context: Context): String {
    val uri = saveBitmapToFile(context, this)
    return uri.toString()
}

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



fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

