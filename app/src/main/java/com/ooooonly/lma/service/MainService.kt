package com.ooooonly.lma.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.compose.runtime.snapshotFlow
import com.ooooonly.lma.MainActivity
import com.ooooonly.lma.R
import com.ooooonly.lma.mirai.BotPhase
import com.ooooonly.lma.model.viewmodel.ViewModelContainer
import com.ooooonly.lma.service.binder.MainServiceBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainService : Service(), CoroutineScope by MainScope() {
    @Inject
    lateinit var viewModelContainer: ViewModelContainer

    companion object {
        const val NOTIFICATION_MAIN = 1
    }

    private val binder by lazy {
        MainServiceBinder(this)
    }

    private val pendingIntent: PendingIntent by lazy {
        Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }
    }

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(
                getString(R.string.service_channel_main_id),
                getText(R.string.service_channel_main_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = getString(R.string.service_channel_main_description) })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMainNotification(message: String): Notification {
        return Notification.Builder(this, getString(R.string.service_channel_main_id))
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(NOTIFICATION_MAIN, getMainNotification(getString(R.string.notification_message).format(0, 0)))
        launch {
            snapshotFlow {
                val onlineBotSize = viewModelContainer.miraiViewModel.botStates.filter { it.phase is BotPhase.Instantiated.Online }.size
                val enableScriptSize = viewModelContainer.scriptViewModel.scripts.filter { it.enabled }.size
                getString(R.string.notification_message).format(onlineBotSize, enableScriptSize)
            }.collect {
                notificationManager.notify(NOTIFICATION_MAIN, getMainNotification(it))
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}