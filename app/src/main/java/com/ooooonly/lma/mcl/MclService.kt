package com.ooooonly.lma.mcl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ooooonly.lma.aidl.IMclService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation.Companion.start
import javax.inject.Inject

@AndroidEntryPoint
class MclService : Service() {

    @Inject lateinit var miraiConsoleImplementation: MiraiConsoleImplementation

    override fun onBind(intent: Intent): IBinder = Binder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        miraiConsoleImplementation.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        miraiConsoleImplementation.cancel()
        super.onDestroy()
    }

    class Binder: IMclService.Stub()
}