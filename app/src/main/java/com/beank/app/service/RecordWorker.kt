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
import com.beank.domain.usecase.GeoUsecases
import com.beank.domain.usecase.setting.GetGeoState
import com.beank.workFlowy.utils.toLocalDateTime
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.Geofence
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
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
    private val notification : NotificationManagerCompat
): CoroutineWorker(applicationContext,workerParams){

    companion object {
        private val random = Random
        suspend fun onGeofenceWork(geofenceId : String, geoState : Int, dateTime: LocalDateTime, geoUsecases: GeoUsecases, context: Context,notification: NotificationManagerCompat){
            val geofenceData = geoUsecases.getChooseGeofence(geofenceId)
            geofenceData?.let {geo ->
                if (geo.geoEvent == geoState) {
                    if (geo.startTime.isAfter(dateTime.toLocalTime()) && geo.endTime.isBefore(dateTime.toLocalTime()) || !geo.timeOption) {//시간안에 동작
                        onRecordReduce(geoUsecases)//현재 까지 진행중이던 기록 저장
                        geoUsecases.insertRecord(//새 기록 등록
                            Record(
                                id = null,
                                tag = geo.tag,
                                date = dateTime.toLocalDate(),
                                startTime = dateTime,
                                endTime = null,
                                progressTime = 0,
                                pause = true
                            )
                        )
                    }
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                        notification.notify(random.nextInt(), notificationBuilder(context, "새로운 기록 시작", "${geofenceData.tag}에 대한 기록을 시작합니다.").build())
                    }
                }
            }
        }

        suspend fun onActivityWork(state : Int, dateTime: LocalDateTime, geoUsecases: GeoUsecases, context: Context, notification: NotificationManagerCompat){
            onRecordReduce(geoUsecases)
            val tag = if (state == ActivityTransition.ACTIVITY_TRANSITION_ENTER) "이동중" else "개인시간"
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
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notification.notify(random.nextInt(),
                    notificationBuilder(context,"새로운 기록 시작","${tag}에 대한 기록을 시작합니다.").build())
            }
        }

        suspend fun onRecordReduce(geoUsecases: GeoUsecases){
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

        suspend fun onGeoStateCheck(getGeoState: GetGeoState) : Boolean {
            return getGeoState().first()
        }
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val geofenceId = inputData.getString("geofenceId")
            val geoState = inputData.getInt("geoState",0)
            val activityState = inputData.getInt("activity",0)
            val dateTime = inputData.getLong("dateTime",0).toLocalDateTime()

            if (geofenceId != null){
                if (geoState == Geofence.GEOFENCE_TRANSITION_EXIT){
                    geoUsecases.updateGeoState(false)
                }else{
                    geoUsecases.updateGeoState(true)
                }
                onGeofenceWork(
                    geofenceId = geofenceId,
                    geoState = geoState,
                    dateTime = dateTime,
                    geoUsecases = geoUsecases,
                    context = applicationContext,
                    notification = notification
                )
            }else{
                if (!onGeoStateCheck(geoUsecases.getGeoState)){
                    onActivityWork(
                        state = activityState,
                        dateTime = dateTime,
                        geoUsecases = geoUsecases,
                        context = applicationContext,
                        notification = notification
                    )
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
        } finally {

        }
    }


}