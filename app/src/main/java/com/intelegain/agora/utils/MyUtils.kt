package com.intelegain.agora.utils

import android.content.Context
import android.widget.Toast

object MyUtils {
    fun showToast(mContext: Context?, msg: String?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }
}