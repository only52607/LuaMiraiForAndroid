package com.ooooonly.lma.ui.extension

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.EmptyView
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class, ExperimentalMaterialApi::class)
@Composable
fun ExtensionScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.extension_title)) },
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        EmptyView(pictureResId = R.drawable.bg_blank_5, messageResId = R.string.empty_screen)
    }
}