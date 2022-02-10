package com.yrc.ddstreamclient.service.ffmpeg.impl

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO
import com.yrc.common.exception.common.ParametersExceptionFacotry
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import com.yrc.common.pojo.ffmpeg.FFmpegConfigItem
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto
import com.yrc.ddstreamclient.dao.ffmpeg.FFmpegProcessMapper
import com.yrc.ddstreamclient.exception.common.EnumClientException
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessBuilder
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessEntity
import com.yrc.ddstreamclient.pojo.ffmpeg.createFromEntity
import com.yrc.ddstreamclient.service.ffmpeg.FFmpegService
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.Resource

@Service
class FFmpegServiceImpl : FFmpegService{

    private val processMap = ConcurrentHashMap<String, Process>()

    @Resource
    private lateinit var ffmpegProcessMapper: FFmpegProcessMapper

    override fun startFFmpeg(processName: String, ffmpegConfigDto: FFmpegConfigDto): FFmpegProcessDto {
        return startFFmpeg(processName, ffmpegConfigDto as FFmpegConfigItem)
    }

    override fun startFFmpeg(processName: String, ffmpegConfigList: List<String>): FFmpegProcessDto {
        return startFFmpeg(processName) {
            ffmpegConfigList
        }
    }

    private fun startFFmpeg(processName: String, ffmpegConfigItem: FFmpegConfigItem): FFmpegProcessDto {
        val ffmpegProcessEntity = when(ffmpegConfigItem) {
            is FFmpegConfigDto -> {
                FFmpegProcessEntity(processName, ffmpegConfigItem)
            }
            else -> {
                FFmpegProcessEntity(processName, ffmpegConfigItem.toList())
            }
        }
        ffmpegProcessMapper.insert(ffmpegProcessEntity)
        if (processMap.containsKey(ffmpegProcessEntity.id)) {
            throw ParametersExceptionFacotry
                .duplicateException(listOf("id" to ffmpegProcessEntity.id))
        }
        val process = FFmpegProcessBuilder(ffmpegConfigItem).start()
        if (process != null) {
            processMap[ffmpegProcessEntity.id
                ?: throw EnumClientException.PROCESS_RUN_ERROR.build()] = process
        }
        return FFmpegProcessDto.createFromEntity(ffmpegProcessEntity, getAliveStatus(process), process)
    }

    override fun stopFFmpegs(ids: List<String>) {
        ids.forEach {
            processMap[it]?.destroy()
        }
    }

    override fun getFFmpegByIds(ids: List<String>): List<FFmpegProcessDto> {
        if (ids.isEmpty()) {
            return listOf()
        }
        return ffmpegProcessMapper.selectBatchIds(ids).map {
            val process = processMap[it.id]
            FFmpegProcessDto.createFromEntity(it, getAliveStatus(process), process)
        }
    }

    override fun getFFmpegByNames(names: List<String>): List<FFmpegProcessDto> {
        if (names.isEmpty()) {
            return listOf()
        }
        val wrapper = KtQueryWrapper(FFmpegProcessEntity::class.java)
            .`in`(FFmpegProcessEntity::name, names)
        return ffmpegProcessMapper.selectList(wrapper).map {
            val process = processMap[it.id]
            FFmpegProcessDto.createFromEntity(it, getAliveStatus(process), process)
        }
    }

    override fun deleteFFmpegProcessByIds(ids: List<String>) {
        if (ids.isEmpty()) {
            return
        }
        ffmpegProcessMapper.deleteBatchIds(ids)
    }

    override fun getFFmpegProcessList(page: Page<FFmpegProcessDto>): IPage<FFmpegProcessDto> {
        val wrapper = KtQueryWrapper(FFmpegProcessEntity::class.java)
        val entityPage = Page<FFmpegProcessEntity>(page.current, page.size, page.total, page.searchCount())
        val selectPage = ffmpegProcessMapper.selectPage(entityPage, wrapper)
        val pageDTO = PageDTO<FFmpegProcessDto>(selectPage.current, selectPage.size, selectPage.total)
        pageDTO.records = selectPage.records.map {
            val process = processMap[it.id]
            FFmpegProcessDto.createFromEntity(it, getAliveStatus(process), process)
        }
        return pageDTO
    }

    override fun getRunningProcessIdList(): List<String> {
        return processMap
            .map { it.key }
    }

    private fun getAliveStatus(process: Process?): Boolean {
        return process?.isAlive ?: false
    }

}