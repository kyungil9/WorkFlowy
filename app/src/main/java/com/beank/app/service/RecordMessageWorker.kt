package com.beank.app.service

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.beank.app.WorkFlowyActivity
import com.beank.app.di.RecordNotification
import com.beank.domain.model.GeofenceData
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.model.Record
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.RecordAlarmUsecases
import com.beank.workFlowy.R
import com.beank.workFlowy.utils.RecordMode
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.zeroFormat
import com.google.android.gms.location.LocationServices
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@HiltWorker
class RecordMessageWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParams: WorkerParameters,
    @RecordNotification private val notification: NotificationManagerCompat,
    private val recordAlarmUsecases: RecordAlarmUsecases,
    private val crashlytics : LogRepository,
    private val recordMessageReceiver: RecordMessageReceiver
) : CoroutineWorker(applicationContext,workerParams) {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        crashlytics.logNonFatalCrash(throwable)
        throwable.printStackTrace()
    }
    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler

    private fun getRecordPendingIntent() : PendingIntent{
        val intent = Intent(applicationContext, RecordMessageReceiver::class.java)
        intent.action = "NEXT_RECORD"
        return PendingIntent.getBroadcast(applicationContext, 0,intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun onRecordInsert(tag: String, dateTime: LocalDateTime){
        recordAlarmUsecases.insertRecord(//새 기록 등록
            Record(
                id = null,
                tag = tag,
                date = dateTime.toLocalDate(),
                startTime = dateTime,
                endTime = null,
                progressTime = 0,
                pause = true
            )
        )
    }

    private suspend fun onRecordReduce(){
        val record = recordAlarmUsecases.getCurrentRecord()
        if (record.date != LocalDate.now()){//하루가 넘게 기록되고있는경우 쪼개기
            var startDay = record.date
            for (i in 0 until ChronoUnit.DAYS.between(record.date, LocalDate.now()).toInt()){
                val startTime = if (i == 0) record.startTime else LocalDateTime.of(startDay, LocalTime.of(0,0,0))
                val endTime = LocalDateTime.of(startDay, LocalTime.of(23, 59, 59))
                val progressTime = Duration.between(startTime, endTime).toMinutes()
                recordAlarmUsecases.insertRecord(
                    Record(
                        id = null,
                        tag = record.tag,
                        date = startDay,
                        startTime = startTime,
                        endTime = endTime,
                        progressTime = progressTime,
                        pause = false
                    )
                )
                startDay = startDay.plusDays(1)
            }
            val today = LocalDate.now()
            val endTime = LocalDateTime.now()
            val startTime = LocalDateTime.of(today, LocalTime.of(0,0))
            val progressTime = Duration.between(startTime, endTime).toMinutes()
            recordAlarmUsecases.updateRecord(
                id = record.id!!,
                startTime = startTime,
                endTime = endTime,
                progressTime = progressTime,
                pause = false,
                date = today
            )
        }else {
            val endTime = LocalDateTime.now()
            val progressTime = Duration.between(record.startTime, endTime).toMinutes()
            recordAlarmUsecases.updateRecord(
                id = record.id!!,
                endTime = endTime,
                progressTime = progressTime,
                pause = false
            )
        }
    }

    private fun createRecordNotification(
        title: String,
        time: String,
        tagId : Int
    ): NotificationCompat.Builder {
        val intent = Intent(applicationContext, WorkFlowyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = applicationContext.getString(R.string.record_channel_id)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(time)
            .setSmallIcon(R.mipmap.workflowy)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        notificationBuilder.setCustomNotification(title, time, tagId)
        return notificationBuilder
    }

    private fun NotificationCompat.Builder.setCustomNotification(
        title: String,
        time: String,
        tagId: Int,
    ): NotificationCompat.Builder {
        val remoteViews = RemoteViews(applicationContext.packageName, com.beank.presentation.R.layout.custom_notification)
        remoteViews.setImageViewResource(com.beank.presentation.R.id.image,
            intToImage(tagId, applicationContext.resources.obtainTypedArray(com.beank.presentation.R.array.tagList))
        )
        remoteViews.setImageViewResource(com.beank.presentation.R.id.nextRecord,
            com.beank.presentation.R.drawable.baseline_navigate_next_24)
        remoteViews.setTextViewText(com.beank.presentation.R.id.title,title)
        remoteViews.setTextViewText(com.beank.presentation.R.id.time,time)
        remoteViews.setOnClickPendingIntent(com.beank.presentation.R.id.nextRecord, getRecordPendingIntent())
        setCustomContentView(remoteViews)
        return this
    }

    private suspend fun onStartRecordNotification(){
        val nowRecord = recordAlarmUsecases.getNowRecord()
        onRecordNotify(nowRecord.record.startTime,nowRecord.record.tag,nowRecord.tag.icon)

    }

    private fun onRecordNotify(startTime : LocalDateTime, tag : String, icon : Int){
        val timeDuration = Duration.between(startTime, LocalDateTime.now())
        val notify = createRecordNotification(tag,
            "${zeroFormat.format(timeDuration.toHours())}:${zeroFormat.format(timeDuration.toMinutes()%60)}", icon)
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            notification.notify(-1,notify.build())
    }

    private suspend fun onStopRecordNotification(){
        recordAlarmUsecases.updateTimePause(false)
        notification.cancel(-1)
    }

    private suspend fun onRecordAlarmCheck() : Boolean {
        return recordAlarmUsecases.getRecordAlarm().first()
    }

    private suspend fun onNextRecordNotification(){
        val currentRecord = recordAlarmUsecases.getCurrentRecord()
        val nextTag = recordAlarmUsecases.getNextTag(currentRecord.tag)
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        var lat = 37.5642135
        var lon = 127.0016985
        if (applicationContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            applicationContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnSuccessListener {
                lat = it.latitude
                lon = it.longitude
            }.await()
        }
        //5분안에 변경된 경우 전에 저장된 record삭제
        recordAlarmUsecases.getTempGeoTrigger()?.let {
            recordAlarmUsecases.removeGeofence(it.id!!)
        }
//        onRecordReduce()
//        onRecordInsert(nextTag.title, LocalDateTime.now()) //5분뒤에 반영??
        recordAlarmUsecases.addTempGeofence(
            GeofenceData(
                enterTag = nextTag.title,
                enterTagImage = nextTag.icon,
                latitude = lat,
                lonitude = lon,
                geoEvent = GeofenceEvent.TempRequest
            )
        )
    }

    private suspend fun onStartReceiver(){
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.SCREEN_ON")
        intentFilter.addAction("android.intent.action.SCREEN_OFF")
        intentFilter.addAction("android.intent.action.TIME_TICK")
        recordAlarmUsecases.updateTimePause(true)
        applicationContext.registerReceiver(recordMessageReceiver,intentFilter)
    }

    private suspend fun onStopReceiver(){
        applicationContext.unregisterReceiver(recordMessageReceiver)
    }


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val mode = inputData.getInt("mode",0)
            withContext(ioDispatchers){
                Log.d("RECORD_ALARM","$mode")
                when(mode){
                    RecordMode.START -> {
                        onStartRecordNotification()
                        onStartReceiver()
                        //계속해서 어떻게 보여줄지 고민??

                    }
                    RecordMode.STOP -> {
                        onStopRecordNotification()
                        onStopReceiver()
                    }
                    RecordMode.REBOOT -> {
                        if (onRecordAlarmCheck()){
                            onStartRecordNotification()
                            onStartReceiver()
                        }
                    }
                    RecordMode.NEXT_RECORD -> {
                        //next tag를 찾아서 현재위치에 대한 geofencedata작성후
                        //addtempgeofence로 추가
                        onNextRecordNotification()
                        onStartRecordNotification()
                    }
                    RecordMode.TICK -> {
                        onStartRecordNotification()
                    }
                }
            }
            Result.success()
        }catch (e : Exception){
            if (runAttemptCount > 3) {
                Log.d("workManager", "try attemt count is over. STOP RETRY")
                Result.success()
            } else {
                Result.retry()
            }
        }
    }

}