package com.intelegain.agora.utils

import android.graphics.Bitmap
import com.android.volley.toolbox.ImageLoader
import com.bumptech.glide.util.LruCache

class LruBitmapCache @JvmOverloads constructor(sizeInKiloBytes: Int = defaultLruCacheSize) : LruCache<String?, Bitmap?>(sizeInKiloBytes.toLong()), ImageLoader.ImageCache {
    override fun getBitmap(url: String): Bitmap {
        return get(url)!!
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }

    companion object {
        val defaultLruCacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
                return maxMemory / 8
            }
    }
}