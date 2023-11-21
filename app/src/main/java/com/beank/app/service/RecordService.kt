package com.beank.app.service

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.beank.app.WorkFlowyActivity
import com.beank.app.di.RecordNotification
import com.beank.domain.model.onSuccess
import com.beank.domain.repository.LogRepository
import com.beank.domain.usecase.RecordAlarmUsecases
import com.beank.workFlowy.R
import com.beank.workFlowy.ui.theme.md_theme_light_inverseOnSurface
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.zeroFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
internal class RecordService : LifecycleService() {

    @Inject lateinit var recordAlarmUsecases: RecordAlarmUsecases
    @Inject @RecordNotification lateinit var notificationManager: NotificationManagerCompat
    @Inject lateinit var crashlytics : LogRepository
    @Inject lateinit var recordMessageReceiver: RecordMessageReceiver

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _,throwable ->
        crashlytics.logNonFatalCrash(throwable)
        throwable.printStackTrace()
    }
    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHandler
    private var notification : NotificationCompat.Builder? = null
    private var recordLayout : RemoteViews? = null
    private var startTime : LocalDateTime? = null
    override fun onCreate() {
        super.onCreate()
        setRecordNotification()

        lifecycleScope.launch(ioDispatchers){
            onStartReceiver()
            recordAlarmUsecases.getNowRecord(true).flowOn(Dispatchers.IO).onEach {state ->
                state.onSuccess {
                    startTime = it.record.startTime
                    val timeDuration = Duration.between(startTime, LocalDateTime.now())
                    recordLayout?.setTags(it.tag.title,it.tag.icon)
                    recordLayout?.setTime("${zeroFormat.format(timeDuration.toHours())}:${zeroFormat.format(timeDuration.toMinutes()%60)}")
                    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
                        notificationManager.notify(NOTIFICATION_ID,notification?.build()!!)
                }
            }.collect()

        }


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent != null){
            val tick = intent.getBooleanExtra("Tick",false)
            if (tick){
                startTime?.let {
                    val timeDuration = Duration.between(startTime, LocalDateTime.now())
                    recordLayout?.setTime("${zeroFormat.format(timeDuration.toHours())}:${zeroFormat.format(timeDuration.toMinutes()%60)}")
                    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
                        notificationManager.notify(NOTIFICATION_ID,notification?.build()!!)
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        onStopReceiver()
        super.onDestroy()
    }

    private suspend fun onStartReceiver(){
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.SCREEN_ON")
        intentFilter.addAction("android.intent.action.SCREEN_OFF")
        intentFilter.addAction("android.intent.action.TIME_TICK")
        recordAlarmUsecases.updateTimePause(true)
        registerReceiver(recordMessageReceiver,intentFilter)
    }

    private fun onStopReceiver(){
        unregisterReceiver(recordMessageReceiver)
    }

    private fun setRecordNotification() {
        val intent = Intent(this, WorkFlowyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = this.getString(R.string.record_channel_id)
        recordLayout = RemoteViews(packageName, com.beank.presentation.R.layout.custom_notification).apply {
            initRecordLayout("", "00:00", 1)
        }

        notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.workflowy)
            .setShowWhen(false)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCustomContentView(recordLayout)

        startForeground(NOTIFICATION_ID,notification?.build())
    }

    private fun RemoteViews.initRecordLayout(
        title: String,
        time: String,
        tagId: Int
    ) : RemoteViews {
        setImageViewResource(com.beank.presentation.R.id.nextRecord,
            com.beank.presentation.R.drawable.baseline_navigate_next_24)
        setOnClickPendingIntent(com.beank.presentation.R.id.nextRecord, getRecordPendingIntent())
        setTags(title, tagId)
        setTime(time)
        return this
    }

    private fun RemoteViews.setTags(
        title: String,
        tagId: Int
    ) : RemoteViews {
        setImageViewResource(com.beank.presentation.R.id.image,
            intToImage(tagId, resources.obtainTypedArray(com.beank.presentation.R.array.tagList))
        )
        setTextViewText(com.beank.presentation.R.id.title,title)
        return this
    }

    private fun RemoteViews.setTime(
        time : String
    ) : RemoteViews {
        setTextViewText(com.beank.presentation.R.id.time,time)
        return this
    }

    private fun getRecordPendingIntent() : PendingIntent{
        val intent = Intent(this, RecordMessageReceiver::class.java)
        intent.action = "NEXT_RECORD"
        return PendingIntent.getBroadcast(this, 0,intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        const val NOTIFICATION_ID = -3
    }

}