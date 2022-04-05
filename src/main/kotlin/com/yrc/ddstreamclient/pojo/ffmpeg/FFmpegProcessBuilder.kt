package com.yrc.ddstreamclient.pojo.ffmpeg

import com.yrc.ddstreamclient.pojo.common.NuProcessHandler
import com.yrc.ddstreamclient.pojo.common.ProcessFormatter
import com.zaxxer.nuprocess.NuProcess
import com.zaxxer.nuprocess.NuProcessBuilder
import org.bytedeco.javacpp.Loader
import java.util.logging.FileHandler

class FFmpegProcessBuilder(
    private val config: List<String>
){
    companion object {
        val ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg::class.java)!!
    }
    fun start(processName: String): NuProcess? {
        val config = ArrayList<String>().apply {
            add(ffmpeg)
            addAll(config)
        }
        val fileHandler = FileHandler("video/log/${processName}.log", 1024 * 256, 1, true).apply {
            formatter = ProcessFormatter()
        }
        val processHandler = NuProcessHandler(processName, fileHandler)
        //TODO("后续要将日志打印到文件，然后提供接口读取")
        val process = NuProcessBuilder(config).let {
            it.setProcessListener(processHandler)
            it.start()
        }

        return process
    }
}