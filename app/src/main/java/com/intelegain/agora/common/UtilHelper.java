package com.intelegain.agora.common;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by suraj.m on 17/8/17.
 */

public class UtilHelper {
    public String loadJSONFromAsset(Context context, String strFileName) {
        String json = null;
        try {
            //InputStream is = getActivity().getAssets().open("getEmpDetails.json");
            InputStream is = context.getAssets().open(strFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
