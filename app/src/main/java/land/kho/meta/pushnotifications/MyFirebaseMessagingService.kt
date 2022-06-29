package land.kho.meta.pushnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.UpdateFcmTokenRequest
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.presentation.main.MainActivity
import land.kho.meta.utils.ExceptionLoggerUtil
import org.apache.commons.text.StringEscapeUtils
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService  : FirebaseMessagingService() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var preferencesUseCase: PreferencesUseCase

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        //Trying to avoid any crash because of API call and preference access in a service.
        // Did not came across any yet, but still a precaution
        try {
            updateNewTokenOnBackend(s)
        }
        catch(e: Exception){
            ExceptionLoggerUtil.logException(e)
        }
        super.onNewToken(s)
    }

    private fun updateNewTokenOnBackend(newToken: String) {

        CoroutineScope(Dispatchers.IO).launch {

            preferencesUseCase.setIsNewFcmTokenSynced(false)

            val response = repository.updateFcmToken(
                UpdateFcmTokenRequest(fcmToken = newToken)
            )
            when(response){
                is NetworkResponse.Error -> {
                    preferencesUseCase.setIsNewFcmTokenSynced(false)
                }
                is NetworkResponse.Success -> {
                    preferencesUseCase.setIsNewFcmTokenSynced(true)
                }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data: Map<String, String> = message.data
        if (message.notification != null) {
            handleNotification1(message)
        } else {
            if (message.data.isNotEmpty()) {
                Log.e("TAG", "Data Payload: " + message.data.toString())
                try {
                    val map = message.data
                    if (map != null) {
                        handleDataMessage(map)
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "Exception: " + e.message)
                }
            }
        }
        super.onMessageReceived(message)
    }


    private fun handleNotification1(notification11: RemoteMessage,action: String? = null, link: String? = null) {

        var notification = notification11.notification
        var type = ""

        if (!NotificationUtils.isAppIsInBackground(applicationContext)) {

            // app is in foreground, broadcast the push message
            val pushNotification = Intent("pushNotification")
            pushNotification.putExtra(
                "message",
                StringEscapeUtils.unescapeJava(notification!!.body)
            )
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

            val title = notification?.title
            val message = StringEscapeUtils.unescapeJava(notification.body)

            System.out.println("MessageBody:-" + title + ",,,,," + notification)

            var resultIntent: Intent? = null

            resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            resultIntent.putExtra("message", message)

            NotificationUtils.showNotificationMessage(
                title,
                message,
                System.currentTimeMillis(),
                resultIntent,
                "",
                applicationContext
            )

        } else {
            // If the app is in background, firebase itself handles the notification
            val pushNotification = Intent("pushNotification")
            pushNotification.putExtra("message", notification!!.body)
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

            val title = StringEscapeUtils.unescapeJava(notification.title)
            val message = StringEscapeUtils.unescapeJava(notification.body)

            var resultIntent: Intent

            resultIntent = Intent(applicationContext, MainActivity::class.java)

            resultIntent.putExtra("message", message)

            NotificationUtils.showNotificationMessage(
                title,
                message,
                System.currentTimeMillis(),
                resultIntent,
                "",
                applicationContext
            )

            val uri =
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.getPackageName() + "/raw/notification");
            val r = RingtoneManager.getRingtone(applicationContext, uri)
            r.play()

        }


    }

    private fun handleDataMessage(map: Map<String, String>) {
        val title = map["title"]
        val type = map["notification_type"]
        val message: String?

        val imageUrl = ""
        val timestamp = System.currentTimeMillis()
        if (!NotificationUtils.isAppIsInBackground(applicationContext)) {
            message = StringEscapeUtils.unescapeJava(map["message"])
            val pushNotification = Intent("pushNotification")
            pushNotification.putExtra("message", message)
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

            var resultIntent: Intent
            resultIntent = Intent(applicationContext, MainActivity::class.java)
            resultIntent.putExtra("message", message)

            if (TextUtils.isEmpty(imageUrl)) {
                NotificationUtils.showNotificationMessage(
                    title,
                    message,
                    timestamp,
                    resultIntent,
                    imageUrl,
                    applicationContext
                )
            } else {
                NotificationUtils.showNotificationMessage(
                    title,
                    message,
                    timestamp,
                    resultIntent,
                    imageUrl,
                    applicationContext
                )
            }
        } else {
            message = StringEscapeUtils.unescapeJava(map["message"])
            var resultIntent: Intent
            resultIntent = Intent(applicationContext, MainActivity::class.java)
            resultIntent.putExtra("message", message)

            if (TextUtils.isEmpty(imageUrl)) {
                NotificationUtils.showNotificationMessage(
                    title,
                    message,
                    timestamp,
                    resultIntent,
                    imageUrl,
                    applicationContext
                )
            } else {
                NotificationUtils.showNotificationMessage(
                    title,
                    message,
                    timestamp,
                    resultIntent,
                    imageUrl,
                    applicationContext
                )
            }

            val uri =
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.getPackageName() + "/raw/notification");
            val r = RingtoneManager.getRingtone(applicationContext, uri)
            r.play()

        }
    }
}