package com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl

enum class VideoCodecType(val codecName: String) {
    COPY("copy"),
    H264("h264"),
    MPEG4("mpeg4")
}