package id.my.anandalukman.pushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONArray
import org.json.JSONObject

/**
 * --- STEP BUILDING NOTIFICATION ---
 * 1) Generate the notification
 * 2) attach the notification created with the custom layout
 * 3) adding constant id, and name
 * 4) show notification
 */

const val channelId = "notification_channel"
const val channelName = "id.my.anandalukman.pushnotification"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.getNotification() != null) {
            generateNotification(remoteMessage.notification?.title!!, remoteMessage.notification?.body!!)
            Log.d("remoteMessage", " ${remoteMessage.data}")
            val jsonObject = JSONObject(remoteMessage.data.toString())
            val date= jsonObject.get("Nick")
            Log.d("remoteNick ", " $date")
        }
    }

    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo_push_notification)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("id.my.anandalukman.pushnotification", R.layout.notification)
        remoteView.setTextViewText(R.id.tv_title_push_notification, title)
        remoteView.setTextViewText(R.id.tv_message_push_notification, message)
        remoteView.setImageViewResource(R.id.ic_push_notification, R.drawable.logo_push_notification)

        return remoteView
    }

}