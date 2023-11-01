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
import com.beank.app.di.NormalNotification
import com.beank.app.utils.notificationBuilder
import com.beank.domain.model.GeofenceData
import com.beank.domain.model.GeofenceEvent
import com.beank.domain.model.Record
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.GeoUsecases
import com.beank.workFlowy.utils.toLocalDateTime
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.Geofence
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
    @NormalNotification private val notification : NotificationManagerCompat,
    private val crashlytics : LogRepository
): CoroutineWorker(applicationContext,workerParams){
    private val random = Random
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        crashlytics.logNonFatalCrash(throwable)
        throwable.printStackTrace()
    }
    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler


    private suspend fun onGeofenceWork(geofenceId : String, geoState : Int, dateTime: LocalDateTime){
        val geofenceData = geoUsecases.getChooseGeofence(geofenceId)
        geofenceData?.let {geo ->
            if (geoState == Geofence.GEOFENCE_TRANSITION_DWELL) {//들어올때 기록저장
                if (geo.geoEvent != GeofenceEvent.ExitRequest){//temp같이 기록
                    onStartRecord(geo,dateTime,true)
                }
            }else{//나갈때 기록 저장
                if (geo.geoEvent == GeofenceEvent.TempRequest){
                    //temp나갈때 삭제 처리
                    geoUsecases.removeGeofence(geofenceId)
                }else if (geo.geoEvent != GeofenceEvent.EnterRequest){
                    //onStartRecord(geo,dateTime,false)
                }
            }
        }
    }

    private suspend fun onStartRecord(geo : GeofenceData, dateTime: LocalDateTime, startState : Boolean){
        if (geo.startTime.isBefore(dateTime.toLocalTime()) && geo.endTime.isAfter(dateTime.toLocalTime()) || !geo.timeOption) {//시간안에 동작
            val record = geoUsecases.getCurrentRecord()
            if(record.tag != if (startState) geo.enterTag else geo.exitTag){
                onRecordReduce()//현재 까지 진행중이던 기록 저장
                onRecordInsert(if (startState) geo.enterTag else geo.exitTag,dateTime)//새 기록 추가
                onNotifySend(if (startState) geo.enterTag else geo.exitTag)//알림 보내기
            }
        }
    }

    private suspend fun onActivityWork(state : Int, dateTime: LocalDateTime){
        val record = geoUsecases.getCurrentRecord()
        val tag = if (state == ActivityTransition.ACTIVITY_TRANSITION_ENTER) "이동중" else "개인시간"
        if (record.tag != tag){//태그가 다를경우만 새로 등록
            onRecordReduce()
            onRecordInsert(tag,dateTime)//새 기록 추가
            onNotifySend(tag)//알림 보내기
        }
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

    private suspend fun onMoveStateCheck() : Boolean {
        return geoUsecases.getMoveState().first()
    }

    private suspend fun onTriggerToggleState() : Boolean {
        return geoUsecases.getTriggerToggle().first()
    }

    private suspend fun onTriggerMoveState() : Boolean {
        return geoUsecases.getMoveTriggerToggle().first()
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val geofenceId = inputData.getString("geofenceId")
            val geoState = inputData.getInt("geoState",0)
            val activityState = inputData.getInt("activity",-1)
            val dateTime = inputData.getLong("dateTime",0).toLocalDateTime()
            val reboot = inputData.getBoolean("reboot", false)

            withContext(ioDispatchers){
                if (geofenceId != null){//지오펜서 트리거 처리
                    if (geoState == Geofence.GEOFENCE_TRANSITION_EXIT){
                        geoUsecases.updateGeoState(false)
                        if (onMoveStateCheck()){
                            onActivityWork(
                                state = ActivityTransition.ACTIVITY_TRANSITION_ENTER,
                                dateTime = LocalDateTime.now()
                            )
                        }
                    }else{
                        geoUsecases.updateGeoState(true)
                        onGeofenceWork(
                            geofenceId = geofenceId,
                            geoState = geoState,
                            dateTime = dateTime
                        )
                    }
                }else if (reboot){//재부팅시 트리거 재등록 처리
                    if (onTriggerToggleState())
                        geoUsecases.startGeofenceToClient()
                    if (onTriggerMoveState())
                        geoUsecases.startMoveToClient()
                }else{//이동중 트리거 처리
                    if (activityState == ActivityTransition.ACTIVITY_TRANSITION_ENTER){
                        geoUsecases.updateMoveState(true)
                    }else{
                        geoUsecases.updateMoveState(false)
                    }
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