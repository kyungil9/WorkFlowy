package com.beank.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beank.workFlowy.utils.RecordMode
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class RecordMessageReceiver : BroadcastReceiver(){
    @Inject lateinit var workManager: WorkManager

    override fun onReceive(context: Context?, intented: Intent?) {
        intented?.let { intent ->
            when(intent.action){//넥스트 기록 눌렀을 경우 작동
                "NEXT_RECORD" ->{
                    val recordMessageWorkRequest = OneTimeWorkRequestBuilder<RecordMessageWorker>()
                        .setInputData(
                            workDataOf(
                            "mode" to RecordMode.NEXT_RECORD
                        )
                        )
                        .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                        .build()
                    workManager.enqueue(recordMessageWorkRequest)
                }
                "android.intent.action.BOOT_COMPLETED","android.intent.action.LOCKED_BOOT_COMPLETED" ->{
                    val recordMessageWorkRequest = OneTimeWorkRequestBuilder<RecordMessageWorker>()
                        .setInputData(
                            workDataOf(
                                "mode" to RecordMode.REBOOT
                            )
                        )
                        .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                        .build()
                    workManager.enqueue(recordMessageWorkRequest)
                }//재부팅시 알림 메시지 다시 띄우기
                else -> {

                }
            }
        }
    }


}