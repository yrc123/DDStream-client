package com.yrc.ddstreamclient.pojo.ffmpeg.proxy.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.proxy.FFmpegProxy

class FFmpegHttpProxy(val proxyUrl: String) : FFmpegProxy {
    companion object {
        private const val HTTP_PROXY_OPTION = "-http_proxy"
    }

    override val proxyType = FFmpegProxyType.HTTP_PROXY
    override fun toList(): List<String> = listOf(HTTP_PROXY_OPTION, proxyUrl)
}