package com.yrc.ddstreamclient.pojo.ffmpeg.proxy.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.proxy.FFmpegProxy

class FFmpegNoProxy : FFmpegProxy {
    override val proxyType = FFmpegProxyType.NO_PROXY

    override fun toList(): List<String> {
        return listOf()
    }
}