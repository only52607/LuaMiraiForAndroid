package com.ooooonly.lma.ui.loginsolver

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.ui.components.dialog.TextFieldDialog
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.network.CustomLoginFailedException
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
fun LoginSolverDialogHost(
    loginSolverDelegate: LoginSolverDelegate
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var solverContinuation:Continuation<String?>? by remember {
        mutableStateOf(null)
    }
    var solverResult by remember {
        mutableStateOf("")
    }
    var currentSolverType:LoginSolverType? by remember {
        mutableStateOf(null)
    }
    val defaultDismissReason = stringResource(R.string.login_solver_dismiss_reason)
    DisposableEffect(loginSolverDelegate) {
        loginSolverDelegate.setSolver(object: LoginSolver() {
            override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
                return suspendCoroutine {
                    currentSolverType = LoginSolverType.PicCaptcha(bot, data)
                    solverContinuation = it
                    showDialog = true
                }
            }

            override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
                return suspendCoroutine {
                    currentSolverType = LoginSolverType.SliderCaptcha(bot, url)
                    solverContinuation = it
                    showDialog = true
                }
            }

            override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
                return suspendCoroutine {
                    currentSolverType = LoginSolverType.UnsafeDeviceLoginVerify(bot, url)
                    solverContinuation = it
                    showDialog = true
                }
            }
        })
        onDispose {
            loginSolverDelegate.clearSolver()
        }
    }
    if (showDialog) {
        TextFieldDialog(
            onDismiss = {
                solverContinuation?.resumeWithException(DismissLoginException(defaultDismissReason))
                showDialog = false
                currentSolverType = null
            },
            onConfirm = {
                solverContinuation?.resume(solverResult)
                showDialog = false
                currentSolverType = null
            },
            content = solverResult,
            onContentChange = { solverResult = it },
            confirmText = stringResource(R.string.login_solver_dialog_confirm),
            dismissText = stringResource(R.string.login_solver_dialog_dismiss),
            titleText = stringResource(R.string.login_solver_dialog_title_default),
            additionalContent = {
                val solverType = currentSolverType
                if (solverType != null) {
                    SolverTypeRender(
                        solverType = solverType,
                        onClick = { solverContinuation?.resume(null) }
                    )
                }
            }
        )
    }
}

class DismissLoginException : CustomLoginFailedException {
    constructor() : super(killBot = true)
    constructor(cause: Throwable?) : super(killBot = true, cause = cause)
    constructor(message: String) : super(killBot = true, message = message)
    constructor(message: String?, cause: Throwable?) : super(true, message, cause)
}