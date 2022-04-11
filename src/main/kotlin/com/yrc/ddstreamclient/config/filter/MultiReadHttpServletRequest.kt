package com.yrc.ddstreamclient.config.filter

import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper


class MultiReadHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    val cachedBytes by lazy {
        IOUtils.toByteArray(super.getInputStream())
    }

    override fun getInputStream(): ServletInputStream {
        return CachedServletInputStream(cachedBytes)
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(inputStream))
    }

    /* An inputstream which reads the cached request body */
    inner class CachedServletInputStream(private val byteArray: ByteArray) : ServletInputStream() {
        val input = ByteArrayInputStream(byteArray)
        private var _readListener: ReadListener? = null

        override fun read(): Int {
            return input.read()
        }

        override fun isFinished(): Boolean {
            return false
        }

        override fun isReady(): Boolean {
            return true
        }

        override fun setReadListener(readListener: ReadListener?) {
            this._readListener = readListener
            if (readListener == null) {
                throw NullPointerException()
            }
        }

    }
}