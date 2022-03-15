package com.yrc.ddstreamclient.controller.ffmpeg

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yrc.common.pojo.common.ResponseDto
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto
import com.yrc.common.utils.ResponseUtils
import com.yrc.ddstreamclient.exception.common.EnumClientException
import com.yrc.ddstreamclient.service.ffmpeg.FFmpegService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/client")
class FFmpegController(
    private val ffmpegService: FFmpegService
) {

    @PostMapping("/ffmpeg/{id}:start")
    fun startPush(@PathVariable id: String, @RequestBody configDto: FFmpegConfigDto): ResponseDto<FFmpegProcessDto> {
        return ResponseUtils
            .successResponse(ffmpegService.startFFmpeg(id, configDto))
    }

    @PostMapping("/ffmpeg/{id}:start-with-list")
    fun startPushWithString(@PathVariable id: String, @RequestBody configList: List<String>): ResponseDto<FFmpegProcessDto> {
        return ResponseUtils
            .successResponse(ffmpegService.startFFmpeg(id, configList))
    }

    @GetMapping("/ffmpeg/{id}:stop")
    fun stopPush(@PathVariable id: String): ResponseDto<String>{
        ffmpegService.stopFFmpegs(listOf(id))
        return ResponseUtils.successStringResponse()
    }

    @DeleteMapping("/ffmpeg/{id}")
    fun deleteProcess(@PathVariable id: String): ResponseDto<String> {
        val process = ffmpegService.getFFmpegByIds(listOf(id)).first()
        if (process.alive) {
            throw EnumClientException.PROCESS_NOT_STOP.build()
        }
        ffmpegService.deleteFFmpegProcessByIds(listOf(id))
        return ResponseUtils.successStringResponse()
    }

    @GetMapping("/ffmpeg")
    fun listProcesses(page: Page<FFmpegProcessDto>): ResponseDto<IPage<FFmpegProcessDto>> {
        return ResponseUtils
            .successResponse(ffmpegService.getFFmpegProcessList(page))
    }

    @GetMapping("/ffmpeg/{id}")
    fun getFFmpegById(@PathVariable id: String): ResponseDto<FFmpegProcessDto> {
        return ResponseUtils
            .successResponse(ffmpegService.getFFmpegByIds(listOf( id)).first())
    }
}