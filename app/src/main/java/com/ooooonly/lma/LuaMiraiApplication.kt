package com.ooooonly.lma

import android.app.Application
import android.util.Log
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import dagger.hilt.android.HiltAndroidApp

const val APP_SECRET = "c8b45524-322a-40ae-8c24-5d5cc8046f7b"

@HiltAndroidApp
class LuaMiraiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Distribute.setEnabled(true)
        Distribute.setEnabledForDebuggableBuild(true)
        AppCenter.start(
            this, APP_SECRET,
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )
        Log.d("Distribute", Distribute.isEnabled().toString())
    }
}