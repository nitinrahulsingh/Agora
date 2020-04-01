package com.intelegain.agora.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Ilya Gazman on 3/6/2016.
 */
class ImageSaver(private val context: Context) {
    private var directoryName = "images"
    private var fileName = "image.png"
    private var external = false
    fun setFileName(fileName: String): ImageSaver {
        this.fileName = fileName
        return this
    }

    fun setExternal(external: Boolean): ImageSaver {
        this.external = external
        return this
    }

    fun setDirectoryName(directoryName: String): ImageSaver {
        this.directoryName = directoryName
        return this
    }

    fun save(bitmapImage: Bitmap) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(createFile())
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun createFile(): File {
        val directory: File
        directory = if (external) {
            getAlbumStorageDir(directoryName)
        } else {
            context.getDir(directoryName, Context.MODE_PRIVATE)
        }
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e("ImageSaver", "Error creating directory $directory")
        }
        return File(directory, fileName)
    }

    private fun getAlbumStorageDir(albumName: String): File {
        return File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName)
    }

    fun load(): Bitmap? {
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(createFile())
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    val filepath: File
        get() = createFile()

    companion object {
        val isExternalStorageWritable: Boolean
            get() {
                val state = Environment.getExternalStorageState()
                return Environment.MEDIA_MOUNTED == state
            }

        val isExternalStorageReadable: Boolean
            get() {
                val state = Environment.getExternalStorageState()
                return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
            }
    }

}