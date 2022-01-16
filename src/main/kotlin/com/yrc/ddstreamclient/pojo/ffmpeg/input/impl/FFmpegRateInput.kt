package com.yrc.ddstreamclient.pojo.ffmpeg.input.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.input.FFmpegInput

class FFmpegRateInput(val inputUri: String, val rate: Boolean = true) : FFmpegInput{
    companion object {
        private const val RATE_OPTION = "-re"
        private const val INPUT_URI_OPTION = "-i"
    }
    override fun toList(): List<String> = listOf(RATE_OPTION, INPUT_URI_OPTION, inputUri)
}