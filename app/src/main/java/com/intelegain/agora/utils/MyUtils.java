package com.intelegain.agora.utils;

import android.content.Context;
import android.widget.Toast;

public class MyUtils {
    public  static  void showToast(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
