package com.yrc.ddstreamclient.config

import com.ecwid.consul.v1.ConsulClient
import com.yrc.common.exception.ip.IpNotGetExcetption
import com.yrc.common.utils.IpUtils
import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConsulConfig {
    companion object {
        private val logger = LogFactory.getLog(ConsulConfig::class.java)!!
    }

    /**
     * 修改原有的[discoveryProperties]的设置
     * 将内网ip替换为公网ip
     */
    @Bean
    @ConditionalOnProperty(value = ["dd-stream.cloud.consul.discovery.auto-ip-address"], havingValue = true.toString())
    fun consulDiscoveryClient(
        consulClient: ConsulClient?,
        discoveryProperties: ConsulDiscoveryProperties?
    ): ConsulDiscoveryClient? {
        try {
            val publicIpAddress = IpUtils.getPublicIpAddress()
            discoveryProperties?.let {
                it.isPreferIpAddress = true
                it.ipAddress = publicIpAddress
            }
        } catch (e: IpNotGetExcetption) {
            logger.error(e.message)
        }
        return ConsulDiscoveryClient(consulClient, discoveryProperties)
    }


}