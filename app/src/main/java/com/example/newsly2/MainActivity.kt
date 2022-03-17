package com.example.newsly2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newsly2.TestWork.Companion.NOTIFICATION_WORK
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        setContent {
            MainScreen()
        }
    }

    private fun init() {
        CoroutineScope(Dispatchers.Default).launch {
            val saveRequest = PeriodicWorkRequestBuilder<TestWork>(15, TimeUnit.MINUTES).build()
//            val request = OneTimeWorkRequest.Builder(TestWork::class.java).build()

            val instanceWorkManager = WorkManager.getInstance(applicationContext)
//            instanceWorkManager.beginUniqueWork(NOTIFICATION_NAME, ExistingWorkPolicy.REPLACE, request).enqueue()
        instanceWorkManager.enqueueUniquePeriodicWork(NOTIFICATION_WORK, ExistingPeriodicWorkPolicy.KEEP, saveRequest)
        }
    }
}