package com.ooooonly.lma

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ooooonly.lma.di.AppContainer
import com.ooooonly.lma.di.ScriptCenterRoot
import com.ooooonly.lma.mcl.MclService
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.service.MainService
import com.ooooonly.lma.ui.LuaMiraiApp
import com.ooooonly.lma.utils.GiteeFile
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LuaMiraiForAndroid)
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "Create")
        setContent {
            LuaMiraiApp(appContainer)
        }
        // startMclService()
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

    private fun startMclService() {
        startService(Intent(this, MclService::class.java))
    }
}