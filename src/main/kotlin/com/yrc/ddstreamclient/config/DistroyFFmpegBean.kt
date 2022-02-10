package com.yrc.ddstreamclient.config

import com.yrc.ddstreamclient.service.ffmpeg.FFmpegService
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.annotation.Configuration
import javax.annotation.Resource

@Configuration
class DistroyFFmpegBean : DisposableBean{
    val logger = LogFactory.getLog(DistroyFFmpegBean::class.java)
    @Resource
    lateinit var ffmpegService: FFmpegService

    override fun destroy() {
        logger.info("停止ffmpeg进程")
        val idList = ffmpegService.getRunningProcessIdList()
        ffmpegService.stopFFmpegs(idList)
    }
}