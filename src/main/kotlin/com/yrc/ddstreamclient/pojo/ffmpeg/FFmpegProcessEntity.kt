package com.yrc.ddstreamclient.pojo.ffmpeg

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto


@TableName(value = "FFMPEG_PROCESS", autoResultMap = true)
data class FFmpegProcessEntity(@TableId(type = IdType.INPUT) var id: String? = null,
                               @TableField(typeHandler = JacksonTypeHandler::class)
                               var config: FFmpegConfigDto? = null,
                               @TableField(typeHandler = JacksonTypeHandler::class)
                               var advancedConfig: List<String>? = null) {
    constructor(id: String, config: FFmpegConfigDto) : this(id, config, null)
    constructor(id: String,advancedConfig: List<String>) : this(id, null, advancedConfig)
}
