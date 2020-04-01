package com.intelegain.agora.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.intelegain.agora.R;
import com.intelegain.agora.activity.Test;


public class MyAlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public MyAlarmService() {
        super("MyAlarmService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification("Wish U happy Birthday! ");
    }

    private void sendNotification(String msg) {

        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Test.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(this).setContentTitle("Birthday Greetings!!!").setSmallIcon(R.drawable.cake)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
        alamNotificationBuilder.setAutoCancel(true);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alamNotificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}



