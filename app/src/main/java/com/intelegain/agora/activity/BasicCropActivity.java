package com.intelegain.agora.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;

import com.intelegain.agora.R;
import com.intelegain.agora.fragmments.BasicFragment;
import com.intelegain.agora.fragmments.MyProfile_MainFragment;
import com.intelegain.agora.utils.ImageSaver;
import com.isseiaoki.simplecropview.util.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicCropActivity extends AppCompatActivity {


    private ExecutorService mExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_crop_basic);
        mExecutor = Executors.newSingleThreadExecutor();

        Uri uri = getIntent().getData();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, BasicFragment.newInstance(String.valueOf(uri))).commit();
        }
        initToolbar();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Basic Sample");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private int calcImageSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        return Math.min(Math.max(metrics.widthPixels, metrics.heightPixels), 2048);
    }

    public class LoadScaledImageTask implements Runnable {
        private Handler mHandler = new Handler(Looper.getMainLooper());
        Context context;
        Uri uri;
        int width;

        public LoadScaledImageTask(Context context, Uri uri, int width) {
            this.context = context;
            this.uri = uri;
            this.width = width;
        }

        @Override
        public void run() {
            final int exifRotation = Utils.getExifOrientation(context, uri);
            int maxSize = Utils.getMaxSize();
            int requestSize = Math.min(width, maxSize);
            try {
                final Bitmap sampledBitmap = Utils.decodeSampledBitmapFromUri(context, uri, requestSize);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        new ImageSaver(BasicCropActivity.this).
                                setFileName("myImage.jpeg").
                                save(sampledBitmap);


                        startActfromSaveImage("myImage.jpeg");
                    }
                });
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void saveBitmap(Uri uri) {
        if (isFinishing()) return;
        mExecutor.submit(new LoadScaledImageTask(this, uri, calcImageSize()));

    }

    public void startActfromSaveImage(String uri) {

        Intent intent = new Intent();
        intent.putExtra("image", uri);
        setResult(MyProfile_MainFragment.IMAGE_CROP, intent);
        finish();

    }


}
