package com.beank.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleAlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var workManager: WorkManager
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == "android.intent.action.BOOT_COMPLETED" || it.action == "android.intent.action.LOCKED_BOOT_COMPLETED"){
                val messageWorkRequest = OneTimeWorkRequestBuilder<MessageWorker>()
                    .setInputData(workDataOf(
                        "reboot" to true
                    ))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(messageWorkRequest)
            }else{
                val title = it.getStringExtra("title").toString()
                val message = it.getStringExtra("body").toString()
                val messageWorkRequest = OneTimeWorkRequestBuilder<MessageWorker>()
                    .setInputData(workDataOf(
                        "local" to true,
                        "title" to title,
                        "body" to message
                    ))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(messageWorkRequest)
            }
        }
    }

}