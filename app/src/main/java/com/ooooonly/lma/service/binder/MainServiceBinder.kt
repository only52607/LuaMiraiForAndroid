package com.ooooonly.lma.service.binder

import android.content.Context
import com.ooooonly.lma.aidl.IBotManager
import com.ooooonly.lma.aidl.ILuaScriptManager
import com.ooooonly.lma.aidl.IMainService

class MainServiceBinder(private val context: Context): IMainService.Stub() {
    private val scriptManager: ILuaScriptManager by lazy {
        LuaScriptManagerBinder(context)
    }

    private val botManager: BotManagerBinder by lazy {
        BotManagerBinder(context)
    }

    override fun getLuaScriptManager(): ILuaScriptManager {
        return scriptManager
    }

    override fun getBotManager(): IBotManager {
        return botManager
    }
}