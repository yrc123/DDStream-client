package com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl

enum class AudioCodecType(val codecType: String) {
    COPY("copy"),
    ACC("aac"),
    APTX("aptx"),
    FLAC("flac"),
    MP3("mp3")

}