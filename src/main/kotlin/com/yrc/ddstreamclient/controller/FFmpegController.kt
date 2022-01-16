package com.yrc.ddstreamclient.controller

import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessBuilder
import org.bytedeco.javacpp.Loader
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/client")
class FFmpegController {
    val processMap = HashMap<String, Process>()

    @Bean
    fun processMap() = processMap

    @GetMapping("/start")
    fun startPush(): FFmpegConfigDto{
        val defaultConfig = FFmpegConfigDto.getDefaultConfig("https://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8", "hls/test.m3u8")
        processMap["test"] = FFmpegProcessBuilder(defaultConfig).start()
        return defaultConfig;
    }
    @GetMapping("/stop")
    fun stopPush(): String{
        processMap["test"]?.let {
            it.destroy()
            return it.isAlive.toString()
        }
        return "Not Found Process"
    }
    @GetMapping("/show")
    fun show(): String{
        val ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg::class.java)
        ProcessBuilder(ffmpeg, "-encoders").inheritIO().start()
        return "ok"
    }
}