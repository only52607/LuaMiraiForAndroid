package com.ooooonly.lma.ui.navigation

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.ooooonly.lma.R
import com.ooooonly.lma.di.AppContainer
import com.ooooonly.lma.mirai.MiraiViewModel
import com.ooooonly.lma.script.ScriptViewModel
import com.ooooonly.lma.ui.about.AboutScreen
import com.ooooonly.lma.ui.bot.BotScreen
import com.ooooonly.lma.ui.bot.edit.EditBotScreen
import com.ooooonly.lma.ui.extension.ExtensionScreen
import com.ooooonly.lma.ui.log.LogScreen
import com.ooooonly.lma.ui.script.ScriptScreen
import com.ooooonly.lma.ui.script.store.ScriptStoreScreen
import com.ooooonly.lma.utils.GiteeFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LuaMiraiNavGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    startDestination: String = Screen.Bot.route,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    appContainer: AppContainer
) {
    val navigationIcon: @Composable () -> Unit = {
        IconButton(onClick = {
            if (scaffoldState.drawerState.isClosed) {
                coroutineScope.launch { scaffoldState.drawerState.open() }
            } else {
                coroutineScope.launch { scaffoldState.drawerState.close() }
            }
        }) {
            Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.drawer_description))
        }
    }
    NavHost(navController = navController, startDestination = startDestination) {
        addBotTopLevel(
            navController = navController,
            miraiViewModel = appContainer.miraiViewModel,
            scaffoldState = scaffoldState,
            navigationIcon = navigationIcon
        )
        addScriptTopLevel(
            navController = navController,
            scriptViewModel = appContainer.scriptViewModel,
            navigationIcon = navigationIcon,
            scaffoldState = scaffoldState,
            scriptCenterRoot = appContainer.scriptCenterRoot,
        )
        addLogTopLevel(
            navController = navController,
            logViewModel = appContainer.logViewModel,
            navigationIcon = navigationIcon
        )
        addAbout(
            navController = navController
        )
        addExtension(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.addBotTopLevel(
    navController: NavController,
    miraiViewModel: MiraiViewModel,
    scaffoldState: ScaffoldState,
    navigationIcon: @Composable () -> Unit
) {
    navigation(
        route = Screen.Bot.route,
        startDestination = LeafScreen.Bot.createRoute(Screen.Bot)
    ) {
        composable(LeafScreen.Bot.createRoute(Screen.Bot)) {
            BotScreen(
                miraiViewModel = miraiViewModel,
                toBotEditScreen = {
                    navController.navigate(LeafScreen.EditBot.createRoute(Screen.Bot, it))
                },
                navigationIcon = navigationIcon
            )
        }
        composable(
            LeafScreen.EditBot.createRoute(Screen.Bot),
            arguments = listOf(navArgument("botId") { nullable = true })
        ) { backStackEntry ->
            EditBotScreen(
                snackbarHostState = scaffoldState.snackbarHostState,
                miraiViewModel = miraiViewModel,
                onBack = { navController.popBackStack() },
                fromBotId = backStackEntry.arguments?.getString("botId")?.toLong()
            )
        }
    }
}

private fun NavGraphBuilder.addScriptTopLevel(
    navController: NavController,
    scriptViewModel: ScriptViewModel,
    scriptCenterRoot: GiteeFile,
    scaffoldState: ScaffoldState,
    navigationIcon: @Composable () -> Unit
) {
    navigation(
        route = Screen.Script.route,
        startDestination = LeafScreen.Script.createRoute(Screen.Script)
    ) {
        composable(LeafScreen.Script.createRoute(Screen.Script)) {
            ScriptScreen(
                toScriptMarket = {
                    navController.navigate(LeafScreen.ScriptStore.createRoute(Screen.Script))
                },
                scriptViewModel = scriptViewModel,
                navigationIcon = navigationIcon
            )
        }
        composable(LeafScreen.ScriptStore.createRoute(Screen.Script)) {
            ScriptStoreScreen(
                scriptCenterRoot = scriptCenterRoot,
                scriptViewModel = scriptViewModel,
                scaffoldState = scaffoldState,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

private fun NavGraphBuilder.addLogTopLevel(
    navController: NavController,
    logViewModel: LogViewModel,
    navigationIcon: @Composable () -> Unit
) {
    navigation(
        route = Screen.Log.route,
        startDestination = LeafScreen.Log.createRoute(Screen.Log)
    ) {
        composable(LeafScreen.Log.createRoute(Screen.Log)) {
            LogScreen(logViewModel = logViewModel, navigationIcon = navigationIcon)
        }
    }
}

private fun NavGraphBuilder.addAbout(
    navController: NavController
) {
    composable(Screen.About.route) {
        AboutScreen(onBack = { navController.popBackStack() })
    }
}

private fun NavGraphBuilder.addExtension(
    navController: NavController
) {
    composable(Screen.Extension.route) {
        ExtensionScreen(onBack = { navController.popBackStack() })
    }
}