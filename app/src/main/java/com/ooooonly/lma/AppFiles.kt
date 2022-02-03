package com.ooooonly.lma

import java.io.File

data class AppFiles(
    val scriptDirectory: File,
    val botWorkingDirBase: File,
    val mclWorkingDirBase: File,
    val odexDirectory: File
)