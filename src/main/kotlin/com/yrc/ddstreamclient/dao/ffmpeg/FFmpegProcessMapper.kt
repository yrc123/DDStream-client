package com.yrc.ddstreamclient.dao.ffmpeg

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface FFmpegProcessMapper : BaseMapper<FFmpegProcessEntity>{
}