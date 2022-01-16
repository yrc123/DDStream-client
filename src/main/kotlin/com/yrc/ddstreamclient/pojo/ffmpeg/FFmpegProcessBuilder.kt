package com.yrc.ddstreamclient.pojo.ffmpeg

import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import org.bytedeco.javacpp.Loader

class FFmpegProcessBuilder(private val config: FFmpegConfigDto){
    companion object {
        val ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg::class.java)
    }
    fun start(): Process {
        val config = ArrayList<String>().apply {
            add(ffmpeg)
            addAll(config.toList())
        }
        return ProcessBuilder(config).inheritIO().start()
    }
}