package com.yrc.ddstreamclient.pojo.ffmpeg.output.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.output.FFmpegOutput

class FFmpegOutput(val outputUri: String, val overrideOutputFiles: Boolean = true) : FFmpegOutput {
    companion object {
        const val COVER_OPTION = "-y"
    }
    override fun toList(): List<String> =
        if (overrideOutputFiles) {
            listOf(COVER_OPTION, outputUri)
        } else {
            listOf(outputUri)
    }
}