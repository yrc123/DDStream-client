package com.yrc.ddstreamclient.pojo.ffmpeg.format.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.format.FFmpegFormat

class FFmpegFlvFormat : FFmpegFormat{
    override val formatType = FFmpegFormatType.FLV
    override fun toList(): List<String> {
        var condition = true;
        return listOf(FFmpegFormat.FORMAT_OPTION, formatType.typeName)
    }
}