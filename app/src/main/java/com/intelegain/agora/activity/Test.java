package com.intelegain.agora.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.intelegain.agora.R;

import java.util.Random;

import pl.droidsonroids.gif.GifTextView;

public class Test extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    int[] imageIds = {
            /*  R.drawable.happybirthday,
              R.drawable.happy_birthday_2,
              R.drawable.happy_birthday_3,
              R.drawable.happy_birthday_4,
  */

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        GifTextView image = (GifTextView) findViewById(R.id.image1);

        Random generator = new Random();
        int randomImageId = imageIds[generator.nextInt(imageIds.length)];
        image.setBackgroundResource(randomImageId);


     /*   mediaPlayer = MediaPlayer.create(this, R.raw.songs);
        mediaPlayer.start();*/


        Button dialogButton = (Button) findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }
}
