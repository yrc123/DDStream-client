package com.yrc.ddstreamclient.service.ffmpeg

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto

interface FFmpegService {

    fun startFFmpeg(processName: String, ffmpegConfigDto: FFmpegConfigDto): FFmpegProcessDto
    fun startFFmpeg(processName: String, ffmpegConfigList: List<String>): FFmpegProcessDto
    fun stopFFmpegs(names: List<String>)
    fun getFFmpegByIds(ids: List<String>): List<FFmpegProcessDto>
    fun getFFmpegByNames(names: List<String>): List<FFmpegProcessDto>
    fun deleteFFmpegProcessByIds(ids: List<String>)
    fun getFFmpegProcessList(page: Page<FFmpegProcessDto>): IPage<FFmpegProcessDto>
    fun getRunningProcessIdList(): List<String>
}