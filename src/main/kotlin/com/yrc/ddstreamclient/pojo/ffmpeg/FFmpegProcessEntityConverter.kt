package com.yrc.ddstreamclient.pojo.ffmpeg

import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto

fun FFmpegProcessDto.Companion.createFromEntity(ffmpegProcessEntity: FFmpegProcessEntity, alive: Boolean, process: Process?): FFmpegProcessDto {
    return FFmpegProcessDto(ffmpegProcessEntity.id!!, ffmpegProcessEntity.name!!, ffmpegProcessEntity.config!!, alive, process)
}