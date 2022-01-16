package com.yrc.ddstreamclient.pojo.ffmpeg.proxy

import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegConfigItem
import com.yrc.ddstreamclient.pojo.ffmpeg.proxy.impl.FFmpegProxyType

interface FFmpegProxy : FFmpegConfigItem{
    val proxyType: FFmpegProxyType
}