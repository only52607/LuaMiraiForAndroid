package com.ooooonly.lma.ui.about

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.ooooonly.lma.BuildConfig
import com.ooooonly.lma.R
import com.ooooonly.lma.utils.asBitmap
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class, ExperimentalMaterialApi::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        val resource = LocalContext.current.resources
        val theme = LocalContext.current.theme
        val context = LocalContext.current
        val icon:Bitmap? = remember {
            val iconDrawable = ResourcesCompat.getDrawable(resource, R.mipmap.ic_launcher, theme) ?: return@remember null
            return@remember iconDrawable.asBitmap()
        }
        LazyColumn {
            item {
                Box(
                    modifier = Modifier.fillParentMaxWidth().padding(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (icon != null) {
                            Card {
                                Image(
                                    bitmap = icon.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.clip(MaterialTheme.shapes.small)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.about_current_version).format(BuildConfig.VERSION_NAME),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }
            item {
                Divider(modifier = Modifier.fillMaxWidth())
            }
            aboutItems(context)
            item {
                Divider(modifier = Modifier.fillMaxWidth())
            }
            item {
                Box(modifier = Modifier.padding(16.dp),) {
                    Text(
                        text = stringResource(R.string.about_disclaimer),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAbout() {
    AboutScreen({})
}