package com.example.waspas.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.waspas.AppMainApplication
import com.example.waspas.MainActivity
import com.example.waspas.R
import com.example.waspas.data.NotificationsInfoTable
import com.example.waspas.model.NotificationInfo
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyFirebaseMessagingService: FirebaseMessagingService(){
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private lateinit var timestamp: String
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val notificationMessage = remoteMessage.data.toString()
            Log.d("MessageReceived2", notificationMessage)
            try {
                val zoneId = ZoneId.of("Asia/Bangkok")
                val notificationInfo = Gson().fromJson(notificationMessage, NotificationInfo::class.java)?: NotificationInfo()
                //this.timestamp = LocalDateTime.now().plusHours(7).format(formatter)
                this.timestamp = LocalDateTime.parse(notificationInfo.timestamp).atZone(zoneId).format(formatter)
            }catch (e: Exception)
            {
                Log.d("MessageReceived", e.message.toString())
            }
            //show Notifications
            //save notification to sql
            val data = Data.Builder()
                .putString("notificationInfo", notificationMessage)
                .putString("timestamp", timestamp)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(data)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(workRequest)
            //Dua ra thong bao
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = 1
            val requestCode = 1
            val channelId = "Firebase Messaging ID"
            val channelName = "Firebase Messaging"
            notificationManager.createNotificationChannel(
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            )

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("notificationInfo", notificationMessage)
            intent.putExtra("timestamp", timestamp)

            val pendingIntentFlag = PendingIntent.FLAG_CANCEL_CURRENT
            val pendingIntent =
                PendingIntent.getActivity(this, requestCode, intent,
                    pendingIntentFlag or PendingIntent.FLAG_IMMUTABLE)

            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Phat hien "/*+ notificationInfo.beeDensity */+  " ong vo ve tan cong")
                .setSmallIcon(R.drawable.bee_removebg_preview)
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(notificationId, notification)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("Retrieve token", "Refreshed token: $token")
    }

}

class NotificationWorker(appContext: Context, workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters)
{
    override suspend fun doWork(): Result {
        val notificationMessage = inputData.getString("notificationInfo")
        val timestamp = inputData.getString("timestamp")?:""
        val notificationInfo = Gson().fromJson(notificationMessage, NotificationInfo::class.java)

        val application = applicationContext as AppMainApplication
        val appRepository = application.container.appRepository
        val notificationsInfoRepository = application.container.notificationsInfoRepository
        val farmID = appRepository.getCameraInfo(notificationInfo.deviceID).farmID

        val notificationInfoTable = NotificationsInfoTable(
            beeDensity = notificationInfo.beeDensity,
            beeInfo = Gson().toJson(notificationInfo.beeInfo),
            deviceID = notificationInfo.deviceID,
            farmID = farmID,
            timestamp = timestamp
        )
        return withContext(Dispatchers.IO) {
            try {
                notificationsInfoRepository.insertNotificationInfo(notificationInfoTable)
                Result.success()
            }catch (e: Exception)
            {
                Log.d("MessageReceivedInsert", e.message.toString())
                Result.failure()
            }
        }
    }

}