package com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.encode.FFmpegAudioCodec

class FFmpegSimpleAudioCodec(val audioType: AudioCodecType) :FFmpegAudioCodec {
    override fun toList(): List<String> {
        return listOf(FFmpegAudioCodec.AUDIO_CODEC_OPTION, audioType.codecType)
    }
}