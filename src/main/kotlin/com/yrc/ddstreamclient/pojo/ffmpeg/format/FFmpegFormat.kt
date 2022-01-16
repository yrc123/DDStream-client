package com.yrc.ddstreamclient.pojo.ffmpeg.format

import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegConfigItem
import com.yrc.ddstreamclient.pojo.ffmpeg.format.impl.FFmpegFormatType

interface FFmpegFormat : FFmpegConfigItem{
    companion object {
        const val FORMAT_OPTION = "-f"
    }
    val formatType: FFmpegFormatType
}