package com.yrc.ddstreamclient.pojo.ffmpeg.encode

import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegConfigItem

fun interface FFmpegAudioCodec : FFmpegConfigItem{
    companion object {
        const val AUDIO_CODEC_OPTION = "-c:a"
        const val AUDIO_BITRATE_OPTION = "-b:a"
    }
}