package com.ooooonly.lma.utils

import java.io.ByteArrayOutputStream
import java.io.OutputStream

fun outputStreamOf(printLine: (String) -> Unit) = object: OutputStream() {
    val buffer = ByteArrayOutputStream()

    override fun flush() {
        if (buffer.size() == 0) return
        printLine(buffer.toString())
        buffer.reset()
    }

    override fun close() {
        flush()
        buffer.close()
    }

    override fun write(b: Int) {
        if (b == '\r'.code) return
        if (b == '\n'.code) {
            flush()
            return
        }
        buffer.write(b)
    }
}