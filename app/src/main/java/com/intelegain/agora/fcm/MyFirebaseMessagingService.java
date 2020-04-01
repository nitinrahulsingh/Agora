package com.intelegain.agora.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.intelegain.agora.R;
import com.intelegain.agora.activity.SplashActivity;
import com.intelegain.agora.database.NotificationDbHelper;
import com.intelegain.agora.database.NotificationReaderContract;
import com.intelegain.agora.utils.Sharedprefrences;


import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFCMMessagingService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "TOKEN_S: " + s);
        Sharedprefrences.getInstance(getApplicationContext()).putString(getString(R.string.key_fcm_token), s);

        Log.d(TAG, "Refreshing GCM Registration Token");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

//        //For registration of token
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//        //To displaying token on logcat
//        Log.d(TAG, "TOKEN: " + refreshedToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {

        String title = message.getNotification().getTitle();    // you will get this in Advance option in FCM console
        String subtitle = message.getNotification().getBody();
        Log.d(TAG, "SERV_NOTIF" + "T: " + title + " s: " + subtitle);

        Map<String, String> data = message.getData();
//        JSONObject object = new JSONObject(data);
//        Log.d(TAG, "JSON_OBJECT: " + object.toString());
//        String strData = "";
//        for (String kkey : data.keySet()) {
//            String val = data.get(kkey);
//            strData += "Key:" + kkey + " value:" + val + "\n";
//        }
//        Log.d(TAG, "Note_Data:\n" + strData);
        String strTitle = data.containsKey("title") ? data.get("title") : "NotFound";   //data.get("title");
        String strMessage = data.containsKey("message") ? data.get("message") : "NotFound"; //data.get("message");
        String strStatus = data.containsKey("status") ? data.get("status") : "NotFound";    //data.get("status");

        /* Storing Notification data in local database */
        NotificationDbHelper mDbHelper = new NotificationDbHelper(getApplicationContext());
// Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NotificationReaderContract.NotificationEntry.COLUMN_NAME_TITLE, strTitle);
        values.put(NotificationReaderContract.NotificationEntry.COLUMN_NAME_SUBTITLE, strMessage);
        values.put(NotificationReaderContract.NotificationEntry.COLUMN_NAME_STATUS, strStatus);
// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(NotificationReaderContract.NotificationEntry.TABLE_NAME, null, values);
        if (newRowId != -1) {
            // notify to Notification_fragment to refresh NotificationList dynamically
            Intent i = new Intent();
            i.setAction("appendChatScreenMsg");
            i.putExtra("newRowId", newRowId);
            i.putExtra("title", strTitle);
            i.putExtra("message", strMessage);
            i.putExtra("status", strStatus);
            this.sendBroadcast(i);
        }

        /*Show Notification*/
        //        sendMyNotification(title, message.getNotification().getBody());

    }

    private void sendMyNotification(String title, String message) {

        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("isFromNotification", "Yes");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        if (title == null) {
            title = "My Firebase Push notification";
        }
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "chanel_id")
//                .setSmallIcon(getNotificationIcon())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_notification_icon : R.mipmap.ic_launcher;
    }

}
