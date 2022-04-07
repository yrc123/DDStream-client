package com.yrc.ddstreamclient.pojo.common

import com.zaxxer.nuprocess.NuAbstractProcessHandler
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.logging.FileHandler
import java.util.logging.Logger

class NuProcessHandler(
    private val processName: String,
    private val fileHandler: FileHandler,
) : NuAbstractProcessHandler(){

    val logger = Logger.getLogger(processName).apply {
        useParentHandlers = false
        addHandler(fileHandler)
    }
    val charSet = StandardCharsets.UTF_8
    val stdoutStringBuffer = StringBuffer()
    val stderrStringBuffer = StringBuffer()

    override fun onStdout(buffer: ByteBuffer, closed: Boolean) {
        commonStd(buffer, closed, stdoutStringBuffer)
    }

    override fun onStderr(buffer: ByteBuffer, closed: Boolean) {
        commonStd(buffer, closed, stderrStringBuffer)
    }

    override fun onExit(statusCode: Int) {
        logger.handlers.forEach {
            it.close()
        }
    }

    private fun commonStd(buffer: ByteBuffer, closed: Boolean, stringBuffer: StringBuffer) {
        if (!closed) {
            val charBuffer = charSet.decode(buffer).asReadOnlyBuffer()
            while (charBuffer.hasRemaining()) {
                val ch = charBuffer.get()
                if (ch == '\r' || ch == '\n' || ch == (-1).toChar()) {
                    if (stringBuffer.length > 1) {
                        logger.info(stringBuffer.toString())
                    }
                    stringBuffer.setLength(0)
                } else {
                    stringBuffer.append(ch)
                }
            }
        }
    }
}