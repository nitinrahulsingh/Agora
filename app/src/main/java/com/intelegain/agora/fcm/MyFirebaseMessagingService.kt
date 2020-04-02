package com.intelegain.agora.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.intelegain.agora.R
import com.intelegain.agora.activity.SplashActivity
import com.intelegain.agora.database.NotificationDbHelper
import com.intelegain.agora.database.NotificationReaderContract
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d(TAG, "TOKEN_S: $s")
        getInstance(applicationContext).putString(getString(R.string.key_fcm_token), s)
        Log.d(TAG, "Refreshing GCM Registration Token")
        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)
        //        //For registration of token
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//        //To displaying token on logcat
//        Log.d(TAG, "TOKEN: " + refreshedToken);
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification!!.title // you will get this in Advance option in FCM console
        val subtitle = message.notification!!.body
        Log.d(TAG, "SERV_NOTIFT: $title s: $subtitle")
        val data = message.data
        //        JSONObject object = new JSONObject(data);
//        Log.d(TAG, "JSON_OBJECT: " + object.toString());
//        String strData = "";
//        for (String kkey : data.keySet()) {
//            String val = data.get(kkey);
//            strData += "Key:" + kkey + " value:" + val + "\n";
//        }
//        Log.d(TAG, "Note_Data:\n" + strData);
        val strTitle = if (data.containsKey("title")) data["title"] else "NotFound" //data.get("title");
        val strMessage = if (data.containsKey("message")) data["message"] else "NotFound" //data.get("message");
        val strStatus = if (data.containsKey("status")) data["status"] else "NotFound" //data.get("status");
        /* Storing Notification data in local database */
        val mDbHelper = NotificationDbHelper(applicationContext)
        // Gets the data repository in write mode
        val db = mDbHelper.writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(NotificationReaderContract.NotificationEntry.COLUMN_NAME_TITLE, strTitle)
        values.put(NotificationReaderContract.NotificationEntry.COLUMN_NAME_SUBTITLE, strMessage)
        values.put(NotificationReaderContract.NotificationEntry.COLUMN_NAME_STATUS, strStatus)
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(NotificationReaderContract.NotificationEntry.TABLE_NAME, null, values)
        if (newRowId != -1L) { // notify to Notification_fragment to refresh NotificationList dynamically
            val i = Intent()
            i.action = "appendChatScreenMsg"
            i.putExtra("newRowId", newRowId)
            i.putExtra("title", strTitle)
            i.putExtra("message", strMessage)
            i.putExtra("status", strStatus)
            this.sendBroadcast(i)
        }
        /*Show Notification*/ //        sendMyNotification(title, message.getNotification().getBody());
    }

    private fun sendMyNotification(title: String, message: String) { //On click of notification it redirect to this Activity
        var title: String? = title
        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra("isFromNotification", "Yes")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        if (title == null) {
            title = "My Firebase Push notification"
        }
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, "chanel_id") //                .setSmallIcon(getNotificationIcon())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private val notificationIcon: Int
        private get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.drawable.ic_notification_icon else R.mipmap.ic_launcher
        }

    companion object {
        private const val TAG = "MyFCMMessagingService"
    }
}