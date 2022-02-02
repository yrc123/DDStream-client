package com.yrc.ddstreamclient.service.ffmpeg.impl

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO
import com.yrc.common.pojo.ffmpeg.FFmpegConfigDto
import com.yrc.ddstreamclient.dao.ffmpeg.FFmpegProcessMapper
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessBuilder
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessDto
import com.yrc.ddstreamclient.pojo.ffmpeg.FFmpegProcessEntity
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
        val ffmpegProcessEntity = FFmpegProcessEntity(processName, ffmpegConfigDto)
        ffmpegProcessMapper.insert(ffmpegProcessEntity)
        if (processMap.containsKey(ffmpegProcessEntity.id)) {
            throw Exception("重复的id")
        }
        val process = FFmpegProcessBuilder(ffmpegConfigDto).start()
        if (process != null) {
            processMap.put(ffmpegProcessEntity.id ?: throw Exception("process运行失败"), process)
        }
        return FFmpegProcessDto(ffmpegProcessEntity, getAliveStatus(process), process)
    }

    override fun stopFFmpegs(ids: List<String>) {
        ids.forEach {
            processMap[it]?.destroy()
        }
    }

    override fun getFFmpegByIds(ids: List<String>): List<FFmpegProcessDto> {
        return ffmpegProcessMapper.selectBatchIds(ids).map {
            val process = processMap[it.id]
            FFmpegProcessDto(it, getAliveStatus(process), process)
        }
    }

    override fun getFFmpegByNames(names: List<String>): List<FFmpegProcessDto> {
        val wrapper = KtQueryWrapper(FFmpegProcessEntity::class.java).`in`(FFmpegProcessEntity::name)
        return ffmpegProcessMapper.selectList(wrapper).map {
            val process = processMap[it.id]
            FFmpegProcessDto(it, getAliveStatus(process), process)
        }
    }

    override fun deleteFFmpegProcessByIds(ids: List<String>) {
        ffmpegProcessMapper.deleteBatchIds(ids)
    }

    override fun getFFmpegProcessList(page: Page<FFmpegProcessDto>): IPage<FFmpegProcessDto> {
        val wrapper = KtQueryWrapper(FFmpegProcessEntity::class.java)
        val entityPage = Page<FFmpegProcessEntity>(page.current, page.size, page.total, page.searchCount())
        val selectPage = ffmpegProcessMapper.selectPage(entityPage, wrapper)
        val pageDTO = PageDTO<FFmpegProcessDto>(selectPage.current, selectPage.size, selectPage.total)
        pageDTO.records = selectPage.records.map {
            val process = processMap[it.id]
            FFmpegProcessDto(it, getAliveStatus(process), process)
        }
        return pageDTO
    }

    override fun getProcessMap(): Map<String, Process> {
        return processMap
    }

    private fun getAliveStatus(process: Process?): Boolean {
        return process?.isAlive ?: false
    }

}