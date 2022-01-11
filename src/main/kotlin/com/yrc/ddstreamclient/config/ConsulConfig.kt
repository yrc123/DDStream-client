package com.yrc.ddstreamclient.config

import com.ecwid.consul.v1.ConsulClient
import com.yrc.common.exception.IpNotGetException
import com.yrc.common.utils.IpUtil
import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConsulConfig {
    companion object {
        private const val IP_NOT_GET_LOG = "无法获取公网ip"
        private val logger = LogFactory.getLog(ConsulConfig::class.java)!!
    }

    /**
     * 修改原有的[discoveryProperties]的设置
     * 将内网ip替换为公网ip
     */
    @Bean
    @ConditionalOnProperty(value = ["spring.cloud.consul.discovery.auto-ip-address"], havingValue = true.toString())
    fun consulDiscoveryClient(
        consulClient: ConsulClient?,
        discoveryProperties: ConsulDiscoveryProperties?
    ): ConsulDiscoveryClient? {
        try {
            val publicIpAddress = IpUtil.getPublicIpAddress()
            discoveryProperties?.let {
                it.isPreferIpAddress = true
                it.ipAddress = publicIpAddress
            }
        } catch (e: IpNotGetException) {
            logger.error(IP_NOT_GET_LOG)
        }
        return ConsulDiscoveryClient(consulClient, discoveryProperties)
    }


}