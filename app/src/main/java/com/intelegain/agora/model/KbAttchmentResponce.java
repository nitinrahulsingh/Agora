package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kalpesh.c on 6/3/18.
 */

public class KbAttchmentResponce {

    @Expose
    @SerializedName("Message")
    public String Message;
    @Expose
    @SerializedName("Status")
    public String Status;
}
