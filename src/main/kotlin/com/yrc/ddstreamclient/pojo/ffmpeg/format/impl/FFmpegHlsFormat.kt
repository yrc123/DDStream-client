package com.yrc.ddstreamclient.pojo.ffmpeg.format.impl

import com.yrc.ddstreamclient.pojo.ffmpeg.format.FFmpegFormat

class FFmpegHlsFormat(val hlsInitTime: Int = DEFAULT_HLS_INIT_TIME,
                      val hlsTime: Int = DEFAULT_HLS_TIME,
                      val hlsListSize: Int = DEFAULT_HLS_LIST_SIZE,
                      val hlsDeleteThreshold: Int = DEFAULT_HLS_DELETE_THRESHOLD,
                      val FFmpegHlsFlags: List<FFmpegHlsFlag> = listOf()
) : FFmpegFormat{
    companion object {
        const val HLS_LIST_SIZE_ALL = 0
        private const val FORMAT_OPTION = "-f"
        private const val HLS_FLAGS_OPTION = "-hls_flags"
        private const val DEFAULT_HLS_INIT_TIME = 0
        private const val DEFAULT_HLS_TIME = 2
        private const val DEFAULT_HLS_LIST_SIZE = 5
        private const val DEFAULT_HLS_DELETE_THRESHOLD = 1
        private const val HLS_INIT_TIME_OPTION = "-hls_init_time"
        private const val HLS_TIME_OPTION = "-hls_time"
        private const val HLS_LIST_SIZE_OPTION = "-hls_list_size"
        private const val HLS_DELETE_THRESHOLD_OPTION = "-hls_delete_threshold"

        fun getDefaultHlsLFormat() = FFmpegHlsFormat(FFmpegHlsFlags = listOf(FFmpegHlsFlag.DELETE_SEGMENTS))
    }
    override val formatType = FFmpegFormatType.HLS

    override fun toList(): List<String> {
        val config = ArrayList<String>().apply {
            add(HLS_INIT_TIME_OPTION); add(hlsInitTime.toString())
            add(HLS_TIME_OPTION); add(hlsTime.toString())
            add(HLS_LIST_SIZE_OPTION); add(hlsListSize.toString())
            add(HLS_DELETE_THRESHOLD_OPTION); add(hlsDeleteThreshold.toString())
            add(FORMAT_OPTION); add(formatType.typeName)
        }
        FFmpegHlsFlags.forEach{
            config.add(HLS_FLAGS_OPTION)
            config.add(it.flagName)
        }
        return config
    }
}