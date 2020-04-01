package com.intelegain.agora.utils

import android.content.Context
import android.os.AsyncTask
import android.util.Base64
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * This class is use to download the given file
 * Created by suraj.m on 14/11/17.
 */
class FileDownloader // constructor
(/*private final String TAG = "FileDownloader";*/
        private val mContext: Context, private val mIOnDownloadTaskFinish: onDownloadTaskFinish) : AsyncTask<String?, Void?, Boolean>() {
    //private Contants2 contants2 = new Contants2();
    var fileName: String? = null

    interface onDownloadTaskFinish {
        fun onDownloadSuccess(strResult: String?, fileName: String?)
        fun onDownloadFailed(strResult: String?)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        //contants2.showProgressDialog(mContext);
    }

    override fun doInBackground(vararg params: String?): Boolean {
        val base64String = params[0] // -> http://maven.apache.org/maven-1.x/maven.pdf
        fileName = params[1] // -> maven.pdf
        //String fileType = params[2];  // type of file data
        val folder = File(mContext.filesDir, Contants2.agora_folder)
        if (!folder.exists()) folder.mkdir() // If directory not exist, create a new one
        val file = File(folder, fileName)
        return try {
            if (file.exists()) file.createNewFile()
            val stringBytes = Base64.decode(base64String, Base64.DEFAULT)
            val os = FileOutputStream(file.absoluteFile, false)
            os.write(stringBytes)
            os.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun onPostExecute(bIsSuccess: Boolean) {
        super.onPostExecute(bIsSuccess)
        // contants2.dismissProgressDialog();
        if (bIsSuccess) { // File download success
            mIOnDownloadTaskFinish.onDownloadSuccess("File download success", fileName)
        } else { // File download Failed
            mIOnDownloadTaskFinish.onDownloadFailed("File download Failed")
        }
    }

    private inner class DownloadFile {
        val MEGABYTE = 1024 * 1024
        fun downloadFile(fileUrl: String?, directory: File?): Boolean {
            var bIsDownlaodSuccess = false
            try {
                val url = URL(fileUrl)
                val urlConnection = url.openConnection() as HttpURLConnection
                //urlConnection.setRequestMethod("GET");
//urlConnection.setDoOutput(true);
                urlConnection.connect()
                val inputStream = urlConnection.inputStream
                val fileOutputStream = FileOutputStream(directory)
                val totalSize = urlConnection.contentLength
                val buffer = ByteArray(MEGABYTE)
                var bufferLength = 0
                while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength)
                }
                fileOutputStream.close()
                bIsDownlaodSuccess = true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bIsDownlaodSuccess
        }
    }

}