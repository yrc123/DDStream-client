package com.yrc.ddstreamclient.pojo.common

import java.util.*
import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

class ProcessFormatter : SimpleFormatter(){

    private val format = "[%1\$tF %1\$tT] %3\$s %n"

    override fun format(lr: LogRecord): String {
        return String.format(format,
            Date(lr.millis),
            lr.level.localizedName,
            lr.message
        )
    }
}