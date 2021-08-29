package com.ooooonly.lma

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.model.viewmodel.ViewModelContainer
import com.ooooonly.lma.service.MainService
import com.ooooonly.lma.ui.LuaMiraiApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var viewModelContainer: ViewModelContainer
    @Inject lateinit var loginSolverDelegate: LoginSolverDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LuaMiraiForAndroid)
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "Create")
        setContent {
            LuaMiraiApp(viewModelContainer, loginSolverDelegate)
        }
    }

    override fun onStop() {
        startMainService()
        super.onStop()
    }

    private fun startMainService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(Intent(this, MainService::class.java))
        }else {
            startService(Intent(this, MainService::class.java))
        }
    }
}