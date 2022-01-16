package com.yrc.ddstreamclient.pojo.ffmpeg.output.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.output.FFmpegOutput

class FFmpegMultiOutput(val outputList: List<FFmpegOutput>) : FFmpegOutput {

    constructor(vararg outputList: FFmpegOutput) : this(outputList.toList())

    override fun toList(): List<String> = outputList.flatMap { it.toList() }
}