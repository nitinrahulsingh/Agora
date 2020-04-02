package com.intelegain.agora.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.intelegain.agora.R
import com.intelegain.agora.fragmments.New_Home_activity
import com.microsoft.windowsazure.notifications.NotificationsHandler

class MyHandler : NotificationsHandler()
{
    private var mNotificationManager: NotificationManager? = null
    var builder: NotificationCompat.Builder? = null
    var ctx: Context? = null
    override fun onReceive(context: Context, bundle: Bundle) {
        ctx = context
        val nhMessage = bundle.getString("message")
        sendNotification(nhMessage)
        if (New_Home_activity.isVisible) {
            New_Home_activity.mainActivity.ToastNotify(nhMessage)
        }
    }

    private fun sendNotification(msg: String?) {
        val intent = Intent(ctx, New_Home_activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mNotificationManager = ctx!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mBuilder = NotificationCompat.Builder(ctx) //                .setSmallIcon(getNotificationIcon())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(ctx!!.resources, R.mipmap.ic_launcher))
                .setContentTitle("Notification Hub Demo")
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setSound(defaultSoundUri)
                .setContentText(msg)
        mBuilder.setContentIntent(contentIntent)
        mNotificationManager!!.notify(NOTIFICATION_ID, mBuilder.build())
    }

    private val notificationIcon: Int
        private get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.drawable.ic_notification_icon else R.mipmap.ic_launcher
        }

    companion object {
        const val NOTIFICATION_ID = 1
    }
}