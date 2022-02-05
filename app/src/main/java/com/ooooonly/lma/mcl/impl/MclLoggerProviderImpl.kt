package com.ooooonly.lma.mcl.impl

import com.ooooonly.lma.mcl.MclLoggerProvider
import com.ooooonly.lma.datastore.entity.LogItem
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.SimpleLogger
import javax.inject.Inject

class MclLoggerProviderImpl @Inject constructor(
    val logViewModel: LogViewModel
): MclLoggerProvider {
    override fun provideMclLogger(identity: String?): MiraiLogger {
        return SimpleLogger { priority: SimpleLogger.LogPriority, message: String?, e: Throwable? ->
            if (priority == SimpleLogger.LogPriority.ERROR) {
                logViewModel.insertLog(
                    LogItem(
                        from = LogItem.FROM_MCL,
                        level = LogItem.LEVEL_ERROR,
                        content = message ?: "",
                        identity = identity.toString()
                    )
                )
            } else {
                logViewModel.insertLog(
                    LogItem(
                        from = LogItem.FROM_MCL,
                        level = LogItem.LEVEL_INFO,
                        content = message ?: "",
                        identity = identity.toString()
                    )
                )
            }
        }
    }
}