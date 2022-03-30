package com.yrc.ddstreamclient.service.ffmpeg

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto

interface FFmpegService {

    fun startFFmpeg(ffmpegProcessDto: FFmpegProcessDto): FFmpegProcessDto
    fun stopFFmpegs(names: List<String>)
    fun getFFmpegByIds(ids: List<String>): List<FFmpegProcessDto>
    fun getFFmpegByNames(names: List<String>): List<FFmpegProcessDto>
    fun deleteFFmpegProcessByIds(ids: List<String>)
    fun getFFmpegProcessList(page: Page<FFmpegProcessDto>): IPage<FFmpegProcessDto>
    fun getRunningProcessIdList(): List<String>
}