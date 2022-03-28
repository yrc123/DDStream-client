package com.yrc.ddstreamclient.exception.common

import com.yrc.common.exception.common.impl.SimpleException
import org.apache.http.HttpStatus

enum class EnumClientException(private val exception: SimpleException) {
    PROCESS_RUN_ERROR(
        SimpleException(HttpStatus.SC_INTERNAL_SERVER_ERROR,
        "process run error")
    ),
    PROCESS_NOT_STOP(
        SimpleException(HttpStatus.SC_BAD_REQUEST,
            "process not stop")
    ),
    NOT_SUPPORT_ENCODE_JWS(
        SimpleException(HttpStatus.SC_BAD_REQUEST,
        "not support encode jws")
    ),
    ;
    fun build(): SimpleException {
        return exception
    }
}