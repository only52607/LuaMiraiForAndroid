package com.ooooonly.lma.bot.utils

import net.mamoe.mirai.network.CustomLoginFailedException
import net.mamoe.mirai.utils.LoginSolver

abstract class LoginSolverDelegate: LoginSolver() {
    abstract fun setSolver(solver: LoginSolver)
    abstract fun clearSolver()
}

class MissLoginSolverException: CustomLoginFailedException(killBot = true, message = "Could not found loginSolver")