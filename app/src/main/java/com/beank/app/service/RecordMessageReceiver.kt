package com.beank.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecordMessageReceiver : BroadcastReceiver(){
    @Inject lateinit var workManager: WorkManager

    override fun onReceive(context: Context?, intented: Intent?) {
        intented?.let { intent ->
            when(intent.action){//넥스트 기록 눌렀을 경우 작동
                "NEXT_RECORD" ->{

                }
                "android.intent.action.BOOT_COMPLETED","android.intent.action.LOCKED_BOOT_COMPLETED" ->{

                }//재부팅시 알림 메시지 다시 띄우기
            }
        }
    }


}