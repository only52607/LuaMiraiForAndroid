package com.ooooonly.lma.service.binder

import android.content.Context
import com.ooooonly.lma.aidl.IBotManager
import com.ooooonly.lma.aidl.ILuaScriptManager
import com.ooooonly.lma.aidl.IMainService

class MainServiceBinder(private val context: Context): IMainService.Stub() {
    override fun getLuaScriptManager(): ILuaScriptManager? {
        return null
    }

    override fun getBotManager(): IBotManager? {
        return null
    }
}