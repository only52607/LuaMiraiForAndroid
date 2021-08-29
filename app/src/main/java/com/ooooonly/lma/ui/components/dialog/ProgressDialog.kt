package com.ooooonly.lma.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@Composable
fun ProgressDialogHost(
    progressDialogState: ProgressDialogState,
    content: (@Composable (ProgressDialogState) -> Unit)? = null
) {
    if (content != null) {
        content(progressDialogState)
    } else {
        for (progressJob in progressDialogState.progressJobs) {
            ProgressDialog(
                onDismissRequest = {
                    progressJob.job.cancel()
                    progressDialogState.progressJobs.remove(progressJob)
                },
                dismissible = progressJob.cancellable,
                dismissText = progressJob.cancelText,
                message = progressJob.message
            )
        }
    }
}

@Composable
fun ProgressDialog(
    message: String,
    dismissible: Boolean = false,
    onDismissRequest: () -> Unit = {},
    dismissText: String = ""
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                if (message.isNotEmpty()) {
                    Text(text = message, Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp))
                }
                if (dismissible) {
                    TextButton(onClick = onDismissRequest) {
                        Text(dismissText)
                    }
                }
            }
        }
    }
}

class ProgressDialogState(
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default
) : CoroutineScope {
    val progressJobs = mutableStateListOf<ProgressJob>()

    fun postProgress(
        message: String,
        onFailure: (Throwable) -> Unit = {},
        cancellable: Boolean = false,
        cancelText: String = "",
        block: suspend () -> Unit,
    ) {
        var onComplete: (() -> Unit)? = null
        val progressJob = ProgressJob(
            job = launch {
                runCatching {
                    block()
                }.onFailure(onFailure)
                onComplete?.invoke()
            },
            message = message,
            cancellable = cancellable,
            cancelText = cancelText
        )
        progressJobs.add(progressJob)
        onComplete = {
            progressJobs.remove(progressJob)
        }
    }
}

class ProgressJob(
    val job: Job,
    val message: String = "",
    val cancellable: Boolean = false,
    val cancelText: String = ""
)

@Composable
fun rememberProgressDialogState() = remember {
    ProgressDialogState()
}