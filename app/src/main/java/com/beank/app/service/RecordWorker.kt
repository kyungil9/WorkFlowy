package com.beank.app.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.contentValuesOf
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.beank.app.utils.notificationBuilder
import com.beank.domain.model.Record
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.GeoUsecases
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.setting.GetGeoState
import com.beank.workFlowy.utils.toLocalDateTime
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.Geofence
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@HiltWorker
class RecordWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val geoUsecases: GeoUsecases,
    private val notification : NotificationManagerCompat,
    private val crashlytics : LogRepository
): CoroutineWorker(applicationContext,workerParams){
    private val random = Random
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        crashlytics.logNonFatalCrash(throwable)
        throwable.printStackTrace()
    }
    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler


    suspend fun onGeofenceWork(geofenceId : String, geoState : Int, dateTime: LocalDateTime){
        val geofenceData = geoUsecases.getChooseGeofence(geofenceId)
        geofenceData?.let {geo ->
            if (geo.geoEvent == geoState) {
                if (geo.startTime.isAfter(dateTime.toLocalTime()) && geo.endTime.isBefore(dateTime.toLocalTime()) || !geo.timeOption) {//시간안에 동작
                    onRecordReduce()//현재 까지 진행중이던 기록 저장
                    onRecordInsert(geo.tag,dateTime)//새 기록 추가
                    onNotifySend(geo.tag)//알림 보내기
                }
            }
        }
    }

    suspend fun onActivityWork(state : Int, dateTime: LocalDateTime){
        onRecordReduce()
        val tag = if (state == ActivityTransition.ACTIVITY_TRANSITION_ENTER) "이동중" else "개인시간"
        onRecordInsert(tag,dateTime)//새 기록 추가
        onNotifySend(tag)//알림 보내기
    }

    private fun onRecordInsert(tag: String, dateTime: LocalDateTime){
        geoUsecases.insertRecord(//새 기록 등록
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

    private fun onNotifySend(tag : String){
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notification.notify(random.nextInt(), notificationBuilder(applicationContext, "새로운 기록 시작", "${tag}에 대한 기록을 시작합니다.").build())
        }
    }

    private suspend fun onRecordReduce(){
        val record = geoUsecases.getCurrentRecord()
        if (record.date != LocalDate.now()){//하루가 넘게 기록되고있는경우 쪼개기
            var startDay = record.date
            for (i in 0 until ChronoUnit.DAYS.between(record.date, LocalDate.now()).toInt()){
                val startTime = if (i == 0) record.startTime else LocalDateTime.of(startDay, LocalTime.of(0,0,0))
                val endTime = LocalDateTime.of(startDay, LocalTime.of(23, 59, 59))
                val progressTime = Duration.between(startTime, endTime).toMinutes()
                geoUsecases.insertRecord(
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
            geoUsecases.updateRecord(
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
            geoUsecases.updateRecord(
                id = record.id!!,
                endTime = endTime,
                progressTime = progressTime,
                pause = false
            )
        }
    }

    private suspend fun onGeoStateCheck() : Boolean {
        return geoUsecases.getGeoState().first()
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val geofenceId = inputData.getString("geofenceId")
            val geoState = inputData.getInt("geoState",0)
            val activityState = inputData.getInt("activity",0)
            val dateTime = inputData.getLong("dateTime",0).toLocalDateTime()

            withContext(ioDispatchers){
                if (geofenceId != null){
                    if (geoState == Geofence.GEOFENCE_TRANSITION_EXIT){
                        geoUsecases.updateGeoState(false)
                    }else{
                        geoUsecases.updateGeoState(true)
                    }
                    onGeofenceWork(
                        geofenceId = geofenceId,
                        geoState = geoState,
                        dateTime = dateTime
                    )
                }else{
                    if (!onGeoStateCheck()){
                        onActivityWork(
                            state = activityState,
                            dateTime = dateTime
                        )
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount > 3) {
                Log.d("workManager", "try attemt count is over. STOP RETRY")
                Result.success()
            } else {
                Result.retry()
            }
        }
    }


}