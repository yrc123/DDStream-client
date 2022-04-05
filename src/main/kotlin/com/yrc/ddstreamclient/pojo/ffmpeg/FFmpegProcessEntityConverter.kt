package com.yrc.ddstreamclient.pojo.ffmpeg

import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto
import com.zaxxer.nuprocess.NuProcess

fun FFmpegProcessDto.Companion.createFromEntity(
    ffmpegProcessEntity: FFmpegProcessEntity,
    alive: Boolean,
    process: NuProcess?
): FFmpegProcessDto {
    return FFmpegProcessDto(
        ffmpegProcessEntity.id!!,
        ffmpegProcessEntity.name!!,
        ffmpegProcessEntity.config,
        ffmpegProcessEntity.advancedConfig,
        alive,
        process)
}