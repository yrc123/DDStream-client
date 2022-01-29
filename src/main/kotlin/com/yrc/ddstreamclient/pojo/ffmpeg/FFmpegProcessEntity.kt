package com.yrc.ddstreamclient.pojo.ffmpeg

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto


@TableName(value = "FFMPEG_PROCESS", autoResultMap = true)
data class FFmpegProcessEntity(@TableId(type = IdType.ASSIGN_UUID) var id: String? = null,
                               var name: String? = null,
                               @TableField(typeHandler = JacksonTypeHandler::class)
                               var config: FFmpegConfigDto? = null) {
    constructor(name: String,config: FFmpegConfigDto) : this(null, name, config)
}
