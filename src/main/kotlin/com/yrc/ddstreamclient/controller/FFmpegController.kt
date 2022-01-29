package com.yrc.ddstreamclient.controller

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessDto
import com.yrc.ddstreamclient.service.ffmpeg.FFmpegService
import org.bytedeco.javacpp.Loader
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

@RestController
@RequestMapping("/api/client")
class FFmpegController {
    @Resource
    lateinit var ffmpegService: FFmpegService

    @PostMapping("/ffmpeg/{name}:start")
    fun startPush(@PathVariable("name") name: String, @RequestBody configDto: FFmpegConfigDto): FFmpegProcessDto {
        return ffmpegService.startFFmpeg(name, configDto)
    }

    @GetMapping("/ffmpeg/{id}:stop")
    fun stopPush(@PathVariable("id") id: String){
        ffmpegService.stopFFmpegs(listOf(id))
    }

    @DeleteMapping("/ffmpeg/{id}")
    fun deleteProcess(@PathVariable("id") id: String) {
        val process = ffmpegService.getFFmpegByIds(listOf(id)).first()
        if (process.alive) {
            TODO("拒绝")
        }
        ffmpegService.deleteFFmpegProcessByIds(listOf(id))
    }

    @PostMapping("/ffmpeg")
    fun listProcesses(@RequestBody page: Page<FFmpegProcessDto>): IPage<FFmpegProcessDto> {
        return ffmpegService.getFFmpegProcessList(page)
    }

    @GetMapping("/test")
    fun test(): FFmpegConfigDto {
        return FFmpegConfigDto.getDefaultConfig("https://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8", "hls/test.m3u8");
    }
    @GetMapping("/show")
    fun show(): String{
        val ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg::class.java)
        ProcessBuilder(ffmpeg, "-encoders").inheritIO().start()
        return "ok"
    }
}