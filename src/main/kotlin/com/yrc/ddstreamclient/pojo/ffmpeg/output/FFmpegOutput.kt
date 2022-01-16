package com.yrc.ddstreamclient.pojo.ffmpeg.output

import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegConfigItem
import com.yrc.ddstreamclient.pojo.ffmpeg.output.impl.FFmpegOutput

fun interface FFmpegOutput : FFmpegConfigItem{
    companion object {
        fun getSimpleOutput(outputUri: String): com.yrc.ddstreamclient.pojo.ffmpeg.output.FFmpegOutput {
            return FFmpegOutput(outputUri)
        }
    }
}