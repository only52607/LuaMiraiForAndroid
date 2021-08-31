package com.ooooonly.lma.mcl.mclinternal

import net.mamoe.mirai.console.plugin.jvm.ExportManager
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(ConsoleExperimentalApi::class)
class ExportManagerImpl(
    private val rules: List<(String) -> Boolean?>
) : ExportManager {

    override fun isExported(className: String): Boolean {
        rules.forEach {
            val result = it(className)
            if (result != null) return@isExported result
        }
        return true
    }

    companion object {
        @JvmStatic
        fun parse(lines: Iterator<String>): ExportManagerImpl {
            fun Boolean.without(value: Boolean) = if (this == value) null else this

            val rules = ArrayList<(String) -> Boolean?>()
            lines.asSequence().map { it.trim() }.filter { it.isNotBlank() }.filterNot {
                it[0] == '#'
            }.forEach { line ->
                val command = line.substringBefore(' ')
                val argument = line.substringAfter(' ', missingDelimiterValue = "").trim()
                val argumentPackage = "$argument."

                when (command) {
                    "exports" -> rules.add {
                        (it == argument || it.startsWith(argumentPackage)).without(false)
                    }
                    "protects" -> rules.add {
                        if (it == argument || it.startsWith(argumentPackage))
                            false
                        else null
                    }
                    "export-all", "export-plugin", "export-system" -> rules.add { true }
                    "protect-all", "protect-plugin", "protect-system" -> rules.add { false }
                }
            }
            return ExportManagerImpl(rules)
        }
    }
}