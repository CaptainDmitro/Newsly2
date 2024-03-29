package org.captaindmitro.newsly2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.captaindmitro.newsly2.NewArticlesWork.Companion.NOTIFICATION_WORK
import org.captaindmitro.newsly2.ui.MainScreen
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
            val saveRequest = PeriodicWorkRequestBuilder<NewArticlesWork>(15, TimeUnit.MINUTES).build()
            val instanceWorkManager = WorkManager.getInstance(applicationContext)

            instanceWorkManager.enqueueUniquePeriodicWork(NOTIFICATION_WORK, ExistingPeriodicWorkPolicy.KEEP, saveRequest)
        }
    }
}