package com.ooooonly.lma.ui.loginsolver

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.ui.components.dialog.SimpleAlertDialog
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.CustomLoginFailedException
import net.mamoe.mirai.utils.LoginSolver
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
fun LoginSolverViewHost(
    loginSolverDelegate: LoginSolverDelegate
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var solverContinuation:Continuation<String?>? by remember {
        mutableStateOf(null)
    }
    var currentSolverData:LoginSolverData? by remember {
        mutableStateOf(null)
    }
    val solverState = rememberSolverState()
    val defaultDismissReason = stringResource(R.string.login_solver_dismiss_reason)
    fun finishLoginSolver(result: String) {
        solverContinuation?.resume(result)
        showDialog = false
        currentSolverData = null
    }
    fun cancelLoginSolver() {
        solverContinuation?.resumeWithException(DismissLoginException(defaultDismissReason))
        showDialog = false
        currentSolverData = null
    }
    DisposableEffect(loginSolverDelegate) {
        loginSolverDelegate.setSolver(object: LoginSolver() {
            override val isSliderCaptchaSupported: Boolean = true

            override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
                return suspendCoroutine {
                    currentSolverData = LoginSolverData.PicCaptcha(bot, data)
                    solverContinuation = it
                    showDialog = true
                }
            }

            override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
                return suspendCoroutine {
                    currentSolverData = LoginSolverData.SliderCaptcha(bot, url)
                    solverContinuation = it
                    showDialog = true
                }
            }

            override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
                return suspendCoroutine {
                    currentSolverData = LoginSolverData.UnsafeDeviceLoginVerify(bot, url)
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
        SimpleAlertDialog (
            onDismiss = ::cancelLoginSolver,
            onConfirm = { finishLoginSolver(solverState.result) },
            confirmText = stringResource(R.string.login_solver_dialog_confirm),
            dismissText = stringResource(R.string.login_solver_dialog_dismiss),
            titleText = stringResource(R.string.login_solver_dialog_title_default),
            content = {
                currentSolverData?.let {
                    SolverDataRender(
                        solverState = solverState,
                        solverData = it,
                        onUpdate = { solverContinuation?.resume(null) },
                        onSolved = ::finishLoginSolver
                    )
                }
            },
        )
    }
}

class DismissLoginException : CustomLoginFailedException {
    constructor() : super(killBot = true)
    constructor(cause: Throwable?) : super(killBot = true, cause = cause)
    constructor(message: String) : super(killBot = true, message = message)
    constructor(message: String?, cause: Throwable?) : super(true, message, cause)
}