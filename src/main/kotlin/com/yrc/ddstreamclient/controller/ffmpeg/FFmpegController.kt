package com.yrc.ddstreamclient.controller.ffmpeg

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yrc.common.pojo.common.ResponseDto
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto
import com.yrc.common.utils.ResponseUtils
import com.yrc.ddstreamclient.exception.common.EnumClientException
import com.yrc.ddstreamclient.service.ffmpeg.FFmpegService
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource

@RestController
@RequestMapping("/api/client")
class FFmpegController {

    @Resource
    lateinit var ffmpegService: FFmpegService

    @PostMapping("/ffmpeg/{name}:start")
    fun startPush(@PathVariable("name") name: String, @RequestBody ffmpegProcessDto: FFmpegProcessDto): ResponseDto<FFmpegProcessDto> {
        return ResponseUtils
            .successResponse(ffmpegService.startFFmpeg(ffmpegProcessDto))
    }

    @GetMapping("/ffmpeg/{name}:stop")
    fun stopPush(@PathVariable("name") name: String): ResponseDto<String>{
        ffmpegService.stopFFmpegs(listOf(name))
        return ResponseUtils.successStringResponse()
    }

    @DeleteMapping("/ffmpeg/{id}:delete-by-id")
    fun deleteProcess(@PathVariable("id") id: String): ResponseDto<String> {
        val process = ffmpegService.getFFmpegByIds(listOf(id)).first()
        if (process.alive == true) {
            throw EnumClientException.PROCESS_NOT_STOP.build()
        }
        ffmpegService.deleteFFmpegProcessByIds(listOf(id))
        return ResponseUtils.successStringResponse()
    }

    @PostMapping("/ffmpeg")
    fun listProcesses(@RequestBody page: Page<FFmpegProcessDto>): ResponseDto<IPage<FFmpegProcessDto>> {
        return ResponseUtils
            .successResponse(ffmpegService.getFFmpegProcessList(page))
    }

    @GetMapping("/ffmpeg/{id}")
    fun getFFmpegById(@PathVariable("id") id: String): ResponseDto<FFmpegProcessDto> {
        return ResponseUtils
            .successResponse(ffmpegService.getFFmpegByIds(listOf( id)).first())
    }
    @GetMapping("/ffmpeg/{name}")
    fun getFFmpegByName(@PathVariable("name") name: String): ResponseDto<List<FFmpegProcessDto>> {
        return ResponseUtils
            .successResponse(ffmpegService.getFFmpegByNames(listOf(name)))
    }
}