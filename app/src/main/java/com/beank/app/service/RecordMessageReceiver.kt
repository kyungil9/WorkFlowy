package com.beank.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.beank.app.utils.goAsync
import com.beank.domain.usecase.setting.GetTimePause
import com.beank.domain.usecase.setting.UpdateTimePause
import com.beank.workFlowy.utils.RecordMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class RecordMessageReceiver : BroadcastReceiver(){
    @Inject lateinit var workManager: WorkManager
    @Inject lateinit var getTimePause: GetTimePause
    @Inject lateinit var updateTimePause: UpdateTimePause

    override fun onReceive(context: Context?, intented: Intent?) = goAsync {
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
                "android.intent.action.LOCKED_BOOT_COMPLETED" ->{
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
                "android.intent.action.SCREEN_ON" -> {
                    Log.d("RECORD_ALARM","screenon")
                    withContext(Dispatchers.IO){
                        updateTimePause(true)
                    }
                }
                "android.intent.action.SCREEN_OFF" -> {
                    Log.d("RECORD_ALARM","screenoff")
                    withContext(Dispatchers.IO){
                        updateTimePause(true)
                    }
                }
                "android.intent.action.TIME_TICK" -> {
                    Log.d("RECORD_ALARM","1111111111")
                    withContext(Dispatchers.IO){
                        if (getTimePause().first()){
                            val recordMessageWorkRequest = OneTimeWorkRequestBuilder<RecordMessageWorker>()
                                .setInputData(
                                    workDataOf(
                                        "mode" to RecordMode.TICK
                                    )
                                )
                                .setBackoffCriteria(BackoffPolicy.LINEAR,30000, TimeUnit.MILLISECONDS)
                                .build()
                            workManager.enqueue(recordMessageWorkRequest)
                        }
                    }

                }
                else -> {

                }
            }
        }
    }


}