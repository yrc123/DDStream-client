package com.yrc.ddstreamclient.service.ffmpeg.impl

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO
import com.yrc.common.exception.common.ParametersExceptionFacotry
import com.yrc.common.pojo.ffmpeg.FFmpegProcessDto
import com.yrc.ddstreamclient.dao.ffmpeg.FFmpegProcessMapper
import com.yrc.ddstreamclient.exception.common.EnumClientException
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessBuilder
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessEntity
import com.yrc.ddstreamclient.pojo.ffmpeg.createFromEntity
import com.yrc.ddstreamclient.service.ffmpeg.FFmpegService
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Service
class FFmpegServiceImpl(
    var ffmpegProcessMapper: FFmpegProcessMapper
) : FFmpegService{

    private val processMap = ConcurrentHashMap<String, FFmpegProcessDto>()
    private val outputUriMap = ConcurrentHashMap<String, String>()

    override fun startFFmpeg(ffmpegProcessDto: FFmpegProcessDto): FFmpegProcessDto {
        val ffmpegConfigItem = if (ffmpegProcessDto.advancedConfig.isNullOrEmpty()) {
            val newOutputSet = ffmpegProcessDto.config?.ffmpegOutputList?.map {
                it.ffmpegOutput?.outputUri
            }?.toSet() ?: setOf()
            synchronized(outputUriMap) {
                val intersectSet = outputUriMap.keys intersect newOutputSet
                if (intersectSet.any { getAliveStatus(processMap[outputUriMap[it]]) }) {
                    throw ParametersExceptionFacotry
                        .duplicateException(
                            intersectSet.map{ "outputUri" to it }
                        )
                } else {
                    newOutputSet.filterNotNull()
                        .forEach{
                            outputUriMap[it] = ffmpegProcessDto.name!!
                        }
                }
            }
            ffmpegProcessDto.config?.toList() ?: listOf()
        } else {
           ffmpegProcessDto.advancedConfig ?: listOf()
        }
        val ffmpegProcessEntity = FFmpegProcessEntity()
        BeanUtils.copyProperties(ffmpegProcessDto, ffmpegProcessEntity)

        synchronized(processMap) {
            if (processMap.containsKey(ffmpegProcessEntity.name)) {
                throw ParametersExceptionFacotry
                    .duplicateException(listOf("name" to ffmpegProcessEntity.name))
            }
            ffmpegProcessMapper.insert(ffmpegProcessEntity)
            val process = FFmpegProcessBuilder(ffmpegConfigItem).start(ffmpegProcessEntity.name!!)
            val resultDto = FFmpegProcessDto.createFromEntity(ffmpegProcessEntity, false, process).apply {
                alive = getAliveStatus(this)
            }
            if (process != null) {
                processMap[ffmpegProcessEntity.name
                    ?: throw EnumClientException.PROCESS_RUN_ERROR.build()] = resultDto
            }
            return resultDto
        }
    }

    override fun stopFFmpegs(names: List<String>) {
        names.forEach {
            processMap[it]?.process?.destroy(true)
            synchronized(outputUriMap) {
                processMap[it]?.config
                    ?.ffmpegOutputList
                    ?.mapNotNull { outputItem -> outputItem.ffmpegOutput?.outputUri }
                    ?.toSet() ?: setOf<String>()
                    .forEach { uri ->
                        if (outputUriMap[uri] == it) {
                            outputUriMap.remove(uri)
                        }
                    }
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

    override fun getProcessByName(name: String): FFmpegProcessDto {
        return processMap[name]?.apply {
            alive = getAliveStatus(this)
        } ?: FFmpegProcessDto(name = name, alive = false)
    }

    private fun getAliveStatus(ffmpegProcessDto: FFmpegProcessDto?): Boolean {
        val alive = ffmpegProcessDto?.process?.isRunning ?: false
        //判断程序是否是alive状态
        return if (alive) {
            //判断在本地推流的视频文件是否已经生成
            val pushStatus = ffmpegProcessDto?.config?.ffmpegOutputList?.filter {
                it.ffmpegOutput
                    ?.outputUri
                    ?.startsWith("video/") ?: false
            }?.map {
                File(it.ffmpegOutput!!.outputUri!!).exists()
            } ?: listOf()
            !pushStatus.any{ exists -> !exists }
        } else {
            false
        }
    }

    private fun getProcessDtoByEntityName(entity: FFmpegProcessEntity): FFmpegProcessDto {
        val processDto = processMap[entity.name]
        return if (processDto?.id == entity.id) {
            FFmpegProcessDto.createFromEntity(entity,
                getAliveStatus(processDto),
                processDto?.process
            )
        } else {
            FFmpegProcessDto.createFromEntity(entity, false, null)
        }
    }

}