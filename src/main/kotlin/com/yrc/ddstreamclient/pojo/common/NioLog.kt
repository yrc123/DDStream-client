package com.yrc.ddstreamclient.pojo.common

import kotlinx.coroutines.yield
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.Selector
import java.nio.charset.StandardCharsets
import java.util.logging.FileHandler
import java.util.logging.Logger

class NioLog(
    private val processName: String,
    private val inputStream: InputStream,
    private val fileHandler: FileHandler,
) {
    val logger = Logger.getLogger(processName).apply {
        useParentHandlers = false
        addHandler(fileHandler)
    }
    val channel = Channels.newChannel(inputStream)
    suspend fun start() {
        val open = Selector.open()
        val byteBuffer = ByteBuffer.allocate(1024)
        val charSet = StandardCharsets.UTF_8
        val stringBuffer = StringBuffer()
        while (true) {
            val len = channel.read(byteBuffer)
            byteBuffer.flip()
            if (len == 0) {
                yield()
                continue
            } else if (len == -1) {
                break
            }
            val charBuffer = charSet.decode(byteBuffer).asReadOnlyBuffer()
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