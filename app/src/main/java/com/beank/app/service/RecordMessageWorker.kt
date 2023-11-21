package com.beank.app.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.beank.domain.model.Record
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.RecordAlarmUsecases
import com.beank.workFlowy.utils.RecordMode
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
    private val recordAlarmUsecases: RecordAlarmUsecases,
    private val crashlytics : LogRepository,
) : CoroutineWorker(applicationContext,workerParams) {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        crashlytics.logNonFatalCrash(throwable)
        throwable.printStackTrace()
    }
    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler

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
        onRecordReduce()
        onRecordInsert(nextTag.title, LocalDateTime.now()) //5분뒤에 반영??
//        recordAlarmUsecases.addTempGeofence(
//            GeofenceData(
//                enterTag = nextTag.title,
//                enterTagImage = nextTag.icon,
//                latitude = lat,
//                lonitude = lon,
//                geoEvent = GeofenceEvent.TempRequest
//            )
//        )
    }





    override suspend fun doWork(): Result = coroutineScope {
        try {
            val mode = inputData.getInt("mode",0)
            withContext(ioDispatchers){
                Log.d("RECORD_ALARM","$mode")
                when(mode){
                    RecordMode.REBOOT -> {
                        if (onRecordAlarmCheck()){
                            val intent = Intent(applicationContext,RecordService::class.java)
                            applicationContext.startService(intent)
                        }
                    }
                    RecordMode.NEXT_RECORD -> {
                        onNextRecordNotification()
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