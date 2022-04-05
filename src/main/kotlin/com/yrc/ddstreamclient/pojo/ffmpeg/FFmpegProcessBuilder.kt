package com.yrc.ddstreamclient.pojo.ffmpeg

import org.bytedeco.javacpp.Loader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter
import kotlin.concurrent.thread

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
        val logger = Logger.getLogger(FFmpegProcessBuilder::class.java.toString())
        logger.useParentHandlers = false
        //TODO("后续要将日志打印到文件，然后提供接口读取")
        val fileHandler = FileHandler("video/log/1.log", 3096, 2, true)
        fileHandler.formatter = SimpleFormatter()
        logger.addHandler(fileHandler)
        val process = ProcessBuilder(config)
//            .redirectOutput(Redirect.to(File("video/log/${processName}.log")))
//            .redirectErrorStream(true)
            .start()
        thread {
            val inputStream = BufferedReader(InputStreamReader(process.errorStream))
            while (true) {
                val line = inputStream.readLine() ?: break
                logger.info(line)
            }
        }
        return process
    }
}