package com.yrc.ddstreamclient.pojo.ffmpeg.encode

import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegConfigItem

fun interface FFmpegVideoCodec : FFmpegConfigItem{
    companion object {
        const val VIDEO_CODEC_OPTION = "-c:v"
        const val VIDEO_BITRATE_OPTION = "-b:v"
        const val VIDEO_MAXRATE_OPTION = "-maxrate"
        const val FPS_OPTION = "-r"
    }
}