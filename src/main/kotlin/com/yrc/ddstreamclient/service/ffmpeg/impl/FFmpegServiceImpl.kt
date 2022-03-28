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

    private val processMap = ConcurrentHashMap<String, FFmpegProcessDto>()
    private val outputUriSet = ConcurrentHashMap<String, Any>().keySet(true)

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
                val newOutputSet = ffmpegConfigItem.ffmpegOutputList?.mapNotNull {
                    it.ffmpegOutput?.outputUri
                }?.toSet() ?: setOf()
                synchronized(outputUriSet) {
                    val intersectSet = outputUriSet intersect newOutputSet
                    if (intersectSet.isNotEmpty()) {
                        throw ParametersExceptionFacotry
                            .duplicateException(
                                intersectSet.map{ "outputUri" to it }
                            )
                    } else {
                        outputUriSet.addAll(newOutputSet)
                    }
                }
                FFmpegProcessEntity(processName, ffmpegConfigItem)
            }
            else -> {
                FFmpegProcessEntity(processName, ffmpegConfigItem.toList())
            }
        }
        synchronized(processMap) {
            if (processMap.containsKey(ffmpegProcessEntity.name)) {
                throw ParametersExceptionFacotry
                    .duplicateException(listOf("name" to ffmpegProcessEntity.name))
            }
            ffmpegProcessMapper.insert(ffmpegProcessEntity)
            val process = FFmpegProcessBuilder(processName, ffmpegConfigItem).start()
            val ffmpegProcessDto = FFmpegProcessDto.createFromEntity(ffmpegProcessEntity, getAliveStatus(process), process)
            if (process != null) {
                processMap[ffmpegProcessEntity.name
                    ?: throw EnumClientException.PROCESS_RUN_ERROR.build()] = ffmpegProcessDto
            }
            return ffmpegProcessDto
        }
    }

    override fun stopFFmpegs(names: List<String>) {
        names.forEach {
            processMap[it]?.process?.destroy()
            synchronized(outputUriSet) {
                outputUriSet.removeAll(processMap[it]?.config
                    ?.ffmpegOutputList
                    ?.mapNotNull { outputItem -> outputItem.ffmpegOutput?.outputUri }
                    ?.toSet() ?: setOf<String>()
                )
            }
            synchronized(processMap) {
                processMap.remove(it)
            }
        }
    }

    override fun getFFmpegByIds(ids: List<String>): List<FFmpegProcessDto> {
        if (ids.isEmpty()) {
            return listOf()
        }
        return ffmpegProcessMapper.selectBatchIds(ids).map {
            getProcessDtoByEntityName(it)
        }
    }

    override fun getFFmpegByNames(names: List<String>): List<FFmpegProcessDto> {
        if (names.isEmpty()) {
            return listOf()
        }
        val wrapper = KtQueryWrapper(FFmpegProcessEntity::class.java)
            .`in`(FFmpegProcessEntity::name, names)
        return ffmpegProcessMapper.selectList(wrapper).map {
            getProcessDtoByEntityName(it)
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
            getProcessDtoByEntityName(it)
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

    private fun getProcessDtoByEntityName(entity: FFmpegProcessEntity): FFmpegProcessDto {
        val processDto = processMap[entity.name]
        return if (processDto?.id == entity.id) {
            FFmpegProcessDto.createFromEntity(entity,
                getAliveStatus(processDto?.process),
                processDto?.process
            )
        } else {
            FFmpegProcessDto.createFromEntity(entity, false, null)
        }
    }

}