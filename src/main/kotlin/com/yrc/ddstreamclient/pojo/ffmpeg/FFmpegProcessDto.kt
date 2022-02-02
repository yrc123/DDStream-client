package com.yrc.ddstreamclient.pojo.ffmpeg

import com.fasterxml.jackson.annotation.JsonIgnore
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto


data class FFmpegProcessDto(val id: String,
                            val name: String,
                            val config: FFmpegConfigDto,
                            var alive: Boolean,
                            @get:JsonIgnore
                            var process: Process?
) {
    constructor(ffmpegProcessEntity: FFmpegProcessEntity, alive: Boolean, process: Process?) :
            this(ffmpegProcessEntity.id!!, ffmpegProcessEntity.name!!, ffmpegProcessEntity.config!!, alive, process)
    fun getProcessInfo(): String{
        return process.toString()
    }
}
