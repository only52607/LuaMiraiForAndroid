package com.ooooonly.lma.mirai.impl

import com.ooooonly.lma.mirai.LoginSolverDelegate
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.CustomLoginFailedException
import net.mamoe.mirai.utils.LoginSolver
import javax.inject.Inject

class LoginSolverDelegateImpl @Inject constructor(): LoginSolverDelegate() {
    private var _solver: LoginSolver? = null

    override fun setSolver(solver: LoginSolver) {
        this._solver = solver
    }

    override fun clearSolver() {
        this._solver = null
    }

    override val isSliderCaptchaSupported: Boolean
        get() {
            val solver = _solver ?: throw MissLoginSolverException()
            return solver.isSliderCaptchaSupported
        }

    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        val solver = _solver ?: throw MissLoginSolverException()
        return solver.onSolvePicCaptcha(bot, data)
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        val solver = _solver ?: throw MissLoginSolverException()
        return solver.onSolveSliderCaptcha(bot, url)
    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        val solver = _solver ?: throw MissLoginSolverException()
        return solver.onSolveUnsafeDeviceLoginVerify(bot, url)
    }
}

class MissLoginSolverException: CustomLoginFailedException(killBot = true, message = "Could not found LoginSolver!")

class DismissLoginException : CustomLoginFailedException {
    constructor() : super(killBot = true)
    constructor(cause: Throwable?) : super(killBot = true, cause = cause)
    constructor(message: String) : super(killBot = true, message = message)
    constructor(message: String?, cause: Throwable?) : super(true, message, cause)
}