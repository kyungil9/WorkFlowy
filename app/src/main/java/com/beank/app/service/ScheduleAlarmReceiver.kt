package com.beank.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beank.workFlowy.utils.MessageMode
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleAlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var workManager: WorkManager
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            Log.d("localMessage","receive intent")
            if (it.action == "android.intent.action.BOOT_COMPLETED" || it.action == "android.intent.action.LOCKED_BOOT_COMPLETED"){
                val messageWorkRequest = OneTimeWorkRequestBuilder<MessageWorker>()
                    .setInputData(workDataOf(
                        "mode" to MessageMode.REBOOT
                    ))
                    .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                    .build()
                workManager.enqueue(messageWorkRequest)
            }else{
                val repeat = it.getBooleanExtra("repeat",false)
                if (repeat){//12시마다 반복
                    val messageWorkRequest = OneTimeWorkRequestBuilder<MessageWorker>()
                        .setInputData(workDataOf("mode" to MessageMode.TODAY))
                        .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                        .build()
                    workManager.enqueue(messageWorkRequest)
                }else{
                    Log.d("localMessage","receive local")
                    val title = it.getStringExtra("title").toString()
                    val message = it.getStringExtra("body").toString()
                    val messageWorkRequest = OneTimeWorkRequestBuilder<MessageWorker>()
                        .setInputData(workDataOf(
                            "mode" to MessageMode.LOCAL,
                            "title" to title,
                            "body" to message
                        ))
                        .setBackoffCriteria(BackoffPolicy.LINEAR,30000,TimeUnit.MILLISECONDS)
                        .build()
                    workManager.enqueue(messageWorkRequest)
                    Log.d("localMessage","receive success")
                }
            }
        }
    }

}