package org.captaindmitro.newsly2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color.RED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsly2.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.captaindmitro.data.repository.RemoteDataSource
import org.captaindmitro.newsly2.utils.DEFAULT_CATEGORY
import org.captaindmitro.newsly2.utils.DEFAULT_COUNTRY
import org.captaindmitro.newsly2.utils.LAST_VISITED_CATEGORY
import org.captaindmitro.newsly2.utils.SELECTED_COUNTRY

@HiltWorker
class NewArticlesWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferences: SharedPreferences
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val NOTIFICATION_ID = "NEWSLY2_NOTIFICATION_ID"
        const val NOTIFICATION_NAME = "Newsly2"
        const val NOTIFICATION_CHANNEL = "NEWSLY2_CHANNEL"
        const val NOTIFICATION_WORK = "NEWSLY2_NOTIFICATION_WORK"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val id = inputData.getInt(NOTIFICATION_ID, 0)

        val country = sharedPreferences.getString(SELECTED_COUNTRY, DEFAULT_COUNTRY)!!
        val category = sharedPreferences.getString(LAST_VISITED_CATEGORY, DEFAULT_CATEGORY)!!

        val article = withContext(Dispatchers.IO) {
            remoteDataSource.topHeadlines(country, category).first()
        }

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

        val pendingIntent = getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
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
}