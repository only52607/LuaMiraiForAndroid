package com.ooooonly.lma.mcl.impl

import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.lma.mcl.MclLoggerProvider
import com.ooooonly.lma.datastore.entity.LogEntity
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
                    LogEntity(
                        from = LogEntity.FROM_MCL,
                        level = LogEntity.LEVEL_ERROR,
                        content = message ?: "",
                        identity = identity.toString()
                    )
                )
            } else {
                logViewModel.insertLog(
                    LogEntity(
                        from = LogEntity.FROM_MCL,
                        level = LogEntity.LEVEL_INFO,
                        content = message ?: "",
                        identity = identity.toString()
                    )
                )
            }
        }
    }
}