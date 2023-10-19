package com.beank.app.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.beank.app.utils.notificationBuilder
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.setting.GetNoticeAlarm
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.random.Random

@HiltWorker
class MessageWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notification : NotificationManagerCompat,
    private val getNoticeAlarm: GetNoticeAlarm,
    private val crashlytics : LogRepository
) : CoroutineWorker(applicationContext,workerParams){
    private val random = Random
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        crashlytics.logNonFatalCrash(throwable)
        throwable.printStackTrace()
    }
    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler



    suspend fun onNoticeAlarmCheck() :Boolean {
        return getNoticeAlarm().first()
    }

    private fun onNotifySend(title : String, body : String){
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notification.notify(random.nextInt(), notificationBuilder(applicationContext, title, body).build())
        }
    }


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val remote = inputData.getBoolean("remote",false)
            val title = inputData.getString("title") ?: ""
            val body = inputData.getString("body") ?: ""
            withContext(ioDispatchers){
                if (remote){
                    if (onNoticeAlarmCheck()){
                        onNotifySend(title, body)
                    }
                }else{
                    //schedule알람 설정
                }
            }
            Result.success()
        }catch (e: Exception) {
            if (runAttemptCount > 3) {
                Log.d("workManager", "try attemt count is over. STOP RETRY")
                Result.success()
            } else {
                Result.retry()
            }
        }
    }

}