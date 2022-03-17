package com.example.newsly2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.RED
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.newsly2.model.Article
import com.example.newsly2.model.Repository
import com.example.newsly2.repository.RemoteDataSource
import com.example.newsly2.repository.RepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltWorker
class TestWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val remoteDataSource: RemoteDataSource
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val NOTIFICATION_ID = "NEWSLY2_NOTIFICATION_ID"
        const val NOTIFICATION_NAME = "Newsly2"
        const val NOTIFICATION_CHANNEL = "NEWSLY2_CHANNEL_0"
        const val NOTIFICATION_WORK = "NESLY2_NOTIFICATION_WORK"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val id = inputData.getInt(NOTIFICATION_ID, 0)
        val article = remoteDataSource.topHeadlines("us", "general").first()

        sendNotification(id, article.title, article.description)

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(id: Int, title: String, text: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(NOTIFICATION_ID, id)
        }

        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.image_placeholder)
            .setContentTitle(title)
            .setContentText(text)
            .setDefaults(DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        notification.setChannelId(NOTIFICATION_CHANNEL)

        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT)

        channel.enableLights(true)
        channel.lightColor = RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(id, notification.build())
    }

//    private fun showNotification() {
//        val intent = Intent(context, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle("Title")
//            .setContentText("Text")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        with (NotificationManagerCompat.from(context)) {
//            notify(0, builder.build())
//        }
//    }
}