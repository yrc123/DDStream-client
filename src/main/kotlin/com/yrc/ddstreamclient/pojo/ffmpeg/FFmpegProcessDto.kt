package com.yrc.ddstreamclient.pojo.ffmpeg

import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto


data class FFmpegProcessDto(val id: String,
                            val name: String,
                            val config: FFmpegConfigDto,
                            var alive: Boolean,
                            var process: Process?
) {
    constructor(ffmpegProcessEntity: FFmpegProcessEntity, alive: Boolean, process: Process?) :
            this(ffmpegProcessEntity.id!!, ffmpegProcessEntity.name!!, ffmpegProcessEntity.config!!, alive, process)
}
