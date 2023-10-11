package com.beank.app.service

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.beank.app.utils.goAsync
import com.beank.app.utils.notificationBuilder
import com.beank.domain.model.Record
import com.beank.domain.usecase.GeoUsecases
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver(){
    @Inject lateinit var geoUsecases: GeoUsecases
    @Inject lateinit var notification : NotificationManagerCompat
    private val random = Random
    override fun onReceive(context : Context?, intent : Intent?) = goAsync {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return@goAsync
            }
        }
        val geofenceTransition = geofencingEvent?.geofenceTransition!!
        if ((geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL)or(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences!!
            onRecordReduce(geoUsecases.getCurrentRecord())//현재 까지 진행중이던 기록 저장
            var tagId : String? = ""
            for (geofence in triggeringGeofences){//들어온 이벤트에서 트리거된거 각자 처리 when
                //record 업데이트 추가
                //현제 record를 가져와서 수정하고 새로운 record를 id이용해서 추가
                //현재를 get으로만 가져오기?
                //시간 체크!!
                val tagId = geoUsecases.getChooseTagId(geofence.requestId)
                tagId?.let {tag ->
                    geoUsecases.insertRecord(//새 기록 등록
                        Record(
                            id = null,
                            tag = tag,//id를 통해서 실제 트리거 tag가져와야할듯?
                            date = LocalDate.now(),
                            startTime = LocalDateTime.now(),
                            endTime = null,
                            progressTime = 0,
                            pause = false
                        )
                    )
                }

            }
            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED && tagId?.isNotEmpty()!!) {
                notification.notify(random.nextInt(),
                    notificationBuilder(context,"새로운 기록 시작","$tagId 에 대한 기록을 시작합니다.").build())
            }

        }else {
            //error

        }


    }


    private fun onRecordReduce(record: Record){
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

    companion object{
        private const val TAG = "GeofenceError"
    }
}

