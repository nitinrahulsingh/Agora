package com.intelegain.agora.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is use to download the given file
 * Created by suraj.m on 14/11/17.
 */
public class FileDownloader extends AsyncTask<String, Void, Boolean> {
    /*private final String TAG = "FileDownloader";*/
    private Context mContext;
    private onDownloadTaskFinish mIOnDownloadTaskFinish;
    //private Contants2 contants2 = new Contants2();
    String fileName;

    public interface onDownloadTaskFinish {
        void onDownloadSuccess(String strResult, String fileName);

        void onDownloadFailed(String strResult);
    }

    // constructor
    public FileDownloader(Context context, onDownloadTaskFinish IOnDownloadTaskFinish) {
        this.mContext = context;
        this.mIOnDownloadTaskFinish = IOnDownloadTaskFinish;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //contants2.showProgressDialog(mContext);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String base64String = params[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
        fileName = params[1];  // -> maven.pdf
        //String fileType = params[2];  // type of file data
        File folder = new File(mContext.getFilesDir(), Contants2.agora_folder);
        if (!folder.exists())
            folder.mkdir(); // If directory not exist, create a new one

        File file = new File(folder, fileName);
        try {

            if (file.exists())
                file.createNewFile();

            byte[] stringBytes = Base64.decode(base64String, Base64.DEFAULT);

            FileOutputStream os = new FileOutputStream(file.getAbsoluteFile(), false);
            os.write(stringBytes);
            os.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    protected void onPostExecute(Boolean bIsSuccess) {
        super.onPostExecute(bIsSuccess);
        // contants2.dismissProgressDialog();
        if (bIsSuccess) {
            // File download success
            mIOnDownloadTaskFinish.onDownloadSuccess("File download success", fileName);
        } else {
            // File download Failed
            mIOnDownloadTaskFinish.onDownloadFailed("File download Failed");
        }
    }

    private class DownloadFile {
        final int MEGABYTE = 1024 * 1024;

        public boolean downloadFile(String fileUrl, File directory) {
            boolean bIsDownlaodSuccess = false;
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();

                bIsDownlaodSuccess = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bIsDownlaodSuccess;
        }
    }
}
