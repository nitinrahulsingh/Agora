package com.intelegain.agora.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.intelegain.agora.R
import pl.droidsonroids.gif.GifTextView
import java.util.*

class Test : AppCompatActivity() {
    var mediaPlayer: MediaPlayer? = null
    var imageIds = intArrayOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val image = findViewById<View>(R.id.image1) as GifTextView
        val generator = Random()
        val randomImageId = imageIds[generator.nextInt(imageIds.size)]
        image.setBackgroundResource(randomImageId)
        /*   mediaPlayer = MediaPlayer.create(this, R.raw.songs);
        mediaPlayer.start();*/
        val dialogButton = findViewById<View>(R.id.dialogButtonOK) as Button
        dialogButton.setOnClickListener {
            mediaPlayer!!.stop()
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer!!.stop()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer!!.start()
    }
}