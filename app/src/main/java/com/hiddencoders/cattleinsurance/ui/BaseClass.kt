package com.hiddencoders.cattleinsurance.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.text.format.DateFormat
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hiddencoders.cattleinsurance.ui.utilis.sharedPreferences.UserSession
import java.io.*
import java.util.*
import javax.inject.Inject

open class BaseClass : AppCompatActivity() {

    @Inject
    lateinit var userSession: UserSession

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString()
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(nomeFoto, ".jpg", storageDir)
        val pictureFilePath = image.absolutePath
        return image
    }

    fun showToast(
        context: Context,
        message: String?
    ) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    open fun convertUsingTraditionalWay(file: File): ByteArray? {
        val fileBytes = ByteArray(file.length().toInt())
        try {
            FileInputStream(file).use { inputStream -> inputStream.read(fileBytes) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return fileBytes
    }

    fun encode(imageUri: File): String {

        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imageUri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val image = BitmapFactory.decodeStream(fis)
        // Encode image to base64 string
        val baos = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        val imageString =
            android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
        return imageString
    }

    fun decode(imageString: String): Bitmap {

        // Decode base64 string to image
        val imageBytes = android.util.Base64.decode(imageString, android.util.Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodedImage
    }
    @RequiresApi(Build.VERSION_CODES.O)
    open fun isBase64(path: String?): Boolean {
        return try {
            Base64.getDecoder().decode(path)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    fun getByteArray(filePath: File): ByteArray {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val mBitmap = BitmapFactory.decodeStream(FileInputStream(filePath), null, options)
        val stream = ByteArrayOutputStream()
        mBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        return byteArray
    }
}