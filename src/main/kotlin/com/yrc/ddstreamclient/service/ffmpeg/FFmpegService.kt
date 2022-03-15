package com.yrc.ddstreamclient.service.ffmpeg

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto

interface FFmpegService {

    fun startFFmpeg(id: String, ffmpegConfigDto: FFmpegConfigDto): FFmpegProcessDto
    fun startFFmpeg(id: String, ffmpegConfigList: List<String>): FFmpegProcessDto
    fun stopFFmpegs(ids: List<String>)
    fun getFFmpegByIds(ids: List<String>): List<FFmpegProcessDto>
    fun deleteFFmpegProcessByIds(ids: List<String>)
    fun getFFmpegProcessList(page: Page<FFmpegProcessDto>): IPage<FFmpegProcessDto>
    fun getRunningProcessIdList(): List<String>
}