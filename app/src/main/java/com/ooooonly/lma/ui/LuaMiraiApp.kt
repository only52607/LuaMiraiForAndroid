package com.ooooonly.lma.ui

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ooooonly.lma.R
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.model.viewmodel.ViewModelContainer
import com.ooooonly.lma.ui.drawer.AppDrawer
import com.ooooonly.lma.ui.loginsolver.LoginSolverViewHost
import com.ooooonly.lma.ui.navigation.LuaMiraiBottomNavigationBar
import com.ooooonly.lma.ui.navigation.LuaMiraiNavGraph
import com.ooooonly.lma.ui.navigation.Screen
import com.ooooonly.lma.ui.theme.LuaMiraiForAndroidTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LuaMiraiApp(
    viewModelContainer: ViewModelContainer,
    loginSolverDelegate: LoginSolverDelegate
) {
    LuaMiraiForAndroidTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
            }
            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val currentSelectedItem by navController.currentScreenAsState()
            val coroutineScope = rememberCoroutineScope()
            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    LuaMiraiBottomNavigationBar(
                        navController = navController,
                        modifier = Modifier.fillMaxWidth(),
                        selectedNavigation = currentSelectedItem
                    )
                },
                snackbarHost = {
                    SnackbarHost(it) { data -> Snackbar(snackbarData = data) }
                },
                drawerContent = {
                    AppDrawer(
                        navController = navController,
                        closeDrawer = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    LuaMiraiNavGraph(
                        navController = navController,
                        scaffoldState = scaffoldState,
                        viewModelContainer = viewModelContainer
                    )
                }
            }
            LoginSolverViewHost(loginSolverDelegate)
            NotificationPermissionCheckDialogHost()
        }
    }
}

@Composable
private fun NotificationPermissionCheckDialogHost() {
    val context = LocalContext.current
    var showGuide by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!(context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager).areNotificationsEnabled()) {
                showGuide = true
            }
        }
    }
    if (showGuide) {
        AlertDialog(
            onDismissRequest = { showGuide = false },
            confirmButton = {
                TextButton(onClick = {
                    context.openPermissionSetting()
                    showGuide = false
                }) { Text(stringResource(R.string.notification_check_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showGuide = false
                }) { Text(stringResource(R.string.dialog_dismiss)) }
            },
            title = {
                Text(
                    text = stringResource(R.string.notification_check_title),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.notification_check_content),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}

@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Bot) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Bot.route } -> {
                    selectedItem.value = Screen.Bot
                }
                destination.hierarchy.any { it.route == Screen.Script.route } -> {
                    selectedItem.value = Screen.Script
                }
                destination.hierarchy.any { it.route == Screen.Log.route } -> {
                    selectedItem.value = Screen.Log
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@SuppressLint("ObsoleteSdkInt")
fun Context.openPermissionSetting() {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("android.provider.extra.APP_PACKAGE", this.packageName)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", this.packageName)
        intent.putExtra("app_uid", this.applicationInfo.uid)
    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + this.packageName)
    } else if (Build.VERSION.SDK_INT >= 15) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        intent.data = Uri.fromParts("package", this.packageName, null)
    }
    startActivity(intent)
}