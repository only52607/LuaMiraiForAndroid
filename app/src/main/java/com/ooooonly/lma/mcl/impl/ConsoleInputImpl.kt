package com.ooooonly.lma.mcl.impl

import net.mamoe.mirai.console.util.ConsoleInput
import javax.inject.Inject

class ConsoleInputImpl @Inject constructor() : ConsoleInput {
    override suspend fun requestInput(hint: String): String {
        return ""
    }
}