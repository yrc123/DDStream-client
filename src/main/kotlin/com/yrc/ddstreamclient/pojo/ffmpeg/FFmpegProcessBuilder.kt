package com.yrc.ddstreamclient.pojo.ffmpeg

import com.yrc.ddstreamclient.pojo.common.NioLog
import com.yrc.ddstreamclient.pojo.common.ProcessFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bytedeco.javacpp.Loader
import java.util.logging.FileHandler

class FFmpegProcessBuilder(
    private val config: List<String>
){
    companion object {
        val ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg::class.java)!!
    }
    fun start(processName: String): Process? {
        val config = ArrayList<String>().apply {
            add(ffmpeg)
            addAll(config)
        }
        //TODO("后续要将日志打印到文件，然后提供接口读取")
        val process = ProcessBuilder(config)
            .start()

        val fileHandler = FileHandler("video/log/${processName}.log", 1024 * 256, 1, true)
        fileHandler.formatter = ProcessFormatter()
        val nioLog = NioLog(processName, process.errorStream, fileHandler)
        CoroutineScope(Dispatchers.IO).launch {
            nioLog.start()
        }
        return process
    }
}