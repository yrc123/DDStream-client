package com.yrc.ddstreamclient.pojo.ffmpeg

import com.yrc.ddstreamclient.pojo.ffmpeg.encode.FFmpegAudioCodec
import com.yrc.ddstreamclient.pojo.ffmpeg.encode.FFmpegVideoCodec
import com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl.AudioCodecType
import com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl.FFmpegSimpleAudioCodec
import com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl.FFmpegSimpleVideoCodec
import com.yrc.ddstreamclient.pojo.ffmpeg.encode.impl.VideoCodecType
import com.yrc.ddstreamclient.pojo.ffmpeg.format.FFmpegFormat
import com.yrc.ddstreamclient.pojo.ffmpeg.format.impl.FFmpegHlsFormat
import com.yrc.ddstreamclient.pojo.ffmpeg.input.FFmpegInput
import com.yrc.ddstreamclient.pojo.ffmpeg.input.impl.FFmpegRateInput
import com.yrc.ddstreamclient.pojo.ffmpeg.output.FFmpegOutput
import com.yrc.ddstreamclient.pojo.ffmpeg.proxy.FFmpegProxy
import com.yrc.ddstreamclient.pojo.ffmpeg.proxy.impl.FFmpegNoProxy

class FFmpegConfig(val ffmpegProxy: FFmpegProxy,
                   val ffmpegInput: FFmpegInput,
                   val ffmpegAudioCodec: FFmpegAudioCodec,
                   val ffmpegVideoCodec: FFmpegVideoCodec,
                   val ffmpegFormat: FFmpegFormat,
                   val ffmpegOutput: FFmpegOutput) {
    companion object {
        fun getDefaultConfig(inputUri: String, outputUri: String): FFmpegConfig{
            return FFmpegConfig(FFmpegNoProxy(),
                FFmpegRateInput(inputUri),
                FFmpegSimpleAudioCodec(AudioCodecType.ACC),
                FFmpegSimpleVideoCodec(VideoCodecType.H264),
                FFmpegHlsFormat.getDefaultHlsLFormat(),
                FFmpegOutput.getSimpleOutput(outputUri))
        }
    }
}