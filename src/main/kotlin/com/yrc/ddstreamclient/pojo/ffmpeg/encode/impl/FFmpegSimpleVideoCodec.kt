package com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.encode.FFmpegVideoCodec

class FFmpegSimpleVideoCodec(val codecType: VideoCodecType) : FFmpegVideoCodec{
    override fun toList(): List<String> {
        return listOf(FFmpegVideoCodec.VIDEO_CODEC_OPTION, codecType.codecName)
    }
}