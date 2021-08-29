package com.ooooonly.lma.ui.drawer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.ui.components.selection.SelectionList
import com.ooooonly.lma.ui.navigation.LeafScreen
import com.ooooonly.lma.ui.navigation.Screen
import com.ooooonly.lma.ui.setting.SettingsActivity

@Composable
fun AppDrawer(
    navController: NavController,
    closeDrawer: () -> Unit
) {
    val context = LocalContext.current
    Image(
        contentDescription = null,
        painter = painterResource(R.drawable.bg_mountain)
    )
    ProvideTextStyle(value = MaterialTheme.typography.h6) {
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.high,
            content = {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Column {
                        Text(stringResource(R.string.app_name_short))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            stringResource(R.string.app_name_description),
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        )
    }

    val docItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.drawer_title_doc,
            contentDescriptionResId = R.string.drawer_title_doc,
            iconImageVector = Icons.Filled.OpenInBrowser,
            onClick = {
                context.startActivity(Intent().apply {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse("https://ooooonly.gitee.io/lua-mirai-doc/#/")
                })
                closeDrawer()
            }
        )
    }

    val storeItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.drawer_title_script_store,
            contentDescriptionResId = R.string.drawer_title_script_store,
            iconImageVector = Icons.Filled.Store,
            onClick = {
                navController.navigate(LeafScreen.ScriptStore.createRoute(Screen.Script))
                closeDrawer()
            }
        )
    }

    val extendLibItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.drawer_title_script_extend_lib,
            contentDescriptionResId = R.string.drawer_title_script_extend_lib,
            iconImageVector = Icons.Filled.Extension,
            onClick = {
                navController.navigate(Screen.Extension.route)
                closeDrawer()
            }
        )
    }

    val settingItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.drawer_title_settings,
            contentDescriptionResId = R.string.drawer_title_settings,
            iconImageVector = Icons.Filled.Settings,
            onClick = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
                closeDrawer()
            }
        )
    }

    val aboutItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.drawer_title_about,
            contentDescriptionResId = R.string.drawer_title_about,
            iconImageVector = Icons.Filled.Info,
            onClick = {
                navController.navigate(Screen.About.route)
                closeDrawer()
            }
        )
    }

    Divider(modifier = Modifier.fillMaxWidth())

    SelectionList(items = listOf(
        docItem,
        storeItem,
        extendLibItem,
        settingItem,
        aboutItem
    ))
}