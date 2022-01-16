package com.yrc.ddstreamclient.config

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class DistroyFFmpegBean : DisposableBean{
    val logger = LogFactory.getLog(DistroyFFmpegBean::class.java)
    @Resource(name = "processMap")
    lateinit var processMap: Map<String, Process>

    override fun destroy() {
        logger.info("停止ffmpeg进程")
        processMap.forEach {
            logger.info("停止${it.value}")
            it.value.destroy()
        }
    }
}