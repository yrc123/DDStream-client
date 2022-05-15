package com.yrc.ddstreamclient.service.ffmpeg.impl

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto
import com.yrc.ddstreamclient.dao.ffmpeg.FFmpegProcessMapper
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessEntity
import com.yrc.ddstreamclient.pojo.ffmpeg.createFromEntity
import com.yrc.ddstreamclient.service.ffmpeg.FFmpegService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.function.Function


@SpringBootTest
@RunWith(SpringRunner::class)
internal class FFmpegServiceImplTest {
    @Autowired
    lateinit var ffmpegService: FFmpegService
    @Autowired
    lateinit var ffmpegProcessMapper: FFmpegProcessMapper
    val processInDbName = "processName"
    lateinit var processEntityInDb: FFmpegProcessEntity
    lateinit var processDtoInDb: FFmpegProcessDto

    @Test
    fun startFFmpeg() {
        val name = "testFFmpeg"
        val ffmpeg = ffmpegService.startFFmpeg(name, listOf(""))
        val processList = ffmpegProcessMapper.selectBatchIds(listOf(ffmpeg.id))
        Assertions.assertThat(processList)
            .hasSize(1)
            .map(Function { it.name })
            .contains(name)
        val idList = ffmpegService.getRunningProcessIdList()
        Assertions.assertThat(idList)
            .hasSize(1)
            .contains(ffmpeg.id)
    }

    @Test
    fun stopFFmpegs() {
        val name = "testFFmpeg"
        val ffmpeg = ffmpegService.startFFmpeg(name, listOf(""))
        ffmpegService.stopFFmpegs(listOf(ffmpeg.id))
    }

    @Test
    fun getFFmpegByIds() {
        val processDtoList = ffmpegService
            .getFFmpegByIds(listOf(processEntityInDb.id!!, "test"))
        Assertions.assertThat(processDtoList)
            .hasSize(1)
        val processDto = processDtoList.first()
        Assertions.assertThat(processDto)
            .isEqualTo(processDtoInDb)
    }

    @Test
    fun getFFmpegByNames() {
        val processDtoList = ffmpegService
            .getFFmpegByNames(listOf(processEntityInDb.name!!, "test"))
        Assertions.assertThat(processDtoList)
            .hasSize(1)
        val processDto = processDtoList.first()
        Assertions.assertThat(processDto)
            .isEqualTo(processDtoInDb)
    }

    @Test
    fun deleteFFmpegProcessByIds() {
        ffmpegService.deleteFFmpegProcessByIds(listOf(processEntityInDb.id!!, "test"))
        val processEntityListInDb = ffmpegProcessMapper.selectBatchIds(listOf(processEntityInDb.id))
        Assertions.assertThat(processEntityListInDb)
            .hasSize(0)
    }

    @Test
    fun getFFmpegProcessList() {
        val name = "testFFmpeg1"
        ffmpegService.startFFmpeg(name, listOf(""))
        val processDtoIPage = ffmpegService.getFFmpegProcessList(Page(1, 1))
        Assertions.assertThat(processDtoIPage.records)
            .hasSize(1)
            .first()
            .isEqualTo(processDtoInDb)
    }

    @AfterEach
    fun stopAllProcess() {
        val idList = ffmpegService.getRunningProcessIdList()
        ffmpegService.stopFFmpegs(idList)
    }

    @BeforeEach
    fun insertProcess() {
        processEntityInDb= FFmpegProcessEntity(processInDbName, listOf(""))
        ffmpegProcessMapper.insert(processEntityInDb)
        processDtoInDb = FFmpegProcessDto
            .createFromEntity(processEntityInDb, false, null)
    }
}