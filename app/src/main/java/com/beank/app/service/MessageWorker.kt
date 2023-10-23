package com.beank.app.service

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.beank.app.utils.notificationBuilder
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.AlarmUsecases
import com.beank.workFlowy.utils.MessageMode
import com.beank.workFlowy.utils.toLong
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.random.Random

@HiltWorker
@RequiresApi(Build.VERSION_CODES.O)
class MessageWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notification : NotificationManagerCompat,
    private val alarmUsecases: AlarmUsecases,
    private val crashlytics : LogRepository,
    private val alarmManager: AlarmManager
) : CoroutineWorker(applicationContext,workerParams){
    private val random = Random
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        crashlytics.logNonFatalCrash(throwable)
        throwable.printStackTrace()
    }
    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler

    private suspend fun onTodayAlarmListUpdate(value : Boolean){//12시 실행
        val dateTime = LocalDateTime.now()
        val alarmList = alarmUsecases.getAlarmSchedule(dateTime.toLocalDate())
        for (schedule in alarmList){//오늘 알람 등록
            if (value){
                if (schedule.alarmTime.isAfter(dateTime))
                    onAlarmInsert(schedule.title,schedule.comment,schedule.alarmTime.toLong(),schedule.alarmCode)
            }else{
                onAlarmInsert(schedule.title,schedule.comment,schedule.alarmTime.toLong(),schedule.alarmCode)
            }
        }
    }

    private suspend fun onAlarmListDelect(){
        val dateTime = LocalDateTime.now()
        val alarmList = alarmUsecases.getAlarmSchedule(dateTime.toLocalDate())
        for (schedule in alarmList){//오늘 알람 등록
            if (dateTime.isAfter(schedule.alarmTime))
                onAlarmDelete(schedule.alarmCode)
        }
    }


    private suspend fun onAlarmInsert(title: String, body: String, time : Long, id: Int){
        val pendingIntent = Intent(applicationContext,ScheduleAlarmReceiver::class.java).let { intent ->
            intent.putExtra("title", title)
            intent.putExtra("body", body)
            PendingIntent.getBroadcast(applicationContext,id,intent,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,//시간대 설정
                    pendingIntent
                )
            }
        }else{
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }

    private suspend fun onAlarmDelete(id : Int){
        val pendingIntent = Intent(applicationContext,ScheduleAlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(applicationContext,id,intent,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)}
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private suspend fun onNoticeAlarmCheck() :Boolean {
        return alarmUsecases.getNoticeAlarm()
    }

    private suspend fun onScheduleAlarmCheck() : Boolean {
        return alarmUsecases.getScheduleAlarm()
    }

    private fun onNotifySend(title : String, body : String){
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notification.notify(random.nextInt(), notificationBuilder(applicationContext, title, body).build())
        }
    }


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val mode = inputData.getInt("mode",-1)
            val title = inputData.getString("title") ?: ""
            val body = inputData.getString("body") ?: ""
            val time = inputData.getLong("time",0)
            val id = inputData.getInt("id",0)
            withContext(ioDispatchers){
                when(mode) {
                    MessageMode.REMOTE-> {
                        Log.d("remoteMessage","workManager success")
                        if (onNoticeAlarmCheck()){//공지 알림 처리
                            onNotifySend(title, body)
                            Log.d("remoteMessage","workManager notify")
                        }
                    }
                    MessageMode.LOCAL -> {//스캐줄 알람 처리
                        if (onScheduleAlarmCheck()) {
                            onNotifySend(title, body)
                        }
                    }
                    MessageMode.REBOOT -> {
                        //오늘 알람 재등록
                        if (onScheduleAlarmCheck()){
                            onTodayAlarmListUpdate(true)
                        }
                    }
                    MessageMode.NOW -> {//오늘 등록한 알람 추가
                        if (onScheduleAlarmCheck()){
                            onAlarmInsert(title,body, time, id)
                        }
                    }
                    MessageMode.CANCLE -> {
                        onAlarmDelete(id)
                    }
                    MessageMode.CANCLEALL -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
                            alarmManager.cancelAll()
                        }else{
                            onAlarmListDelect()
                        }
                    }
                    MessageMode.TODAY -> {
                        //schedule알람 설정 -> workmanager 12 00분마다 실행
                        if (onScheduleAlarmCheck()){
                            onTodayAlarmListUpdate(false)
                        }
                    }
                    else -> {}
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