// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.util.starter.testutil

import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.JsonJacksonCodec
import org.redisson.config.Config
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.MapPropertySource

@Configuration
class ClientConfiguration {
    @Bean
    fun redissonClient(): RedissonClient {
        val objectMapper = ObjectMapper().findAndRegisterModules()
        val config = Config()
            .setCodec(JsonJacksonCodec(objectMapper))
            .apply {
                useSingleServer()
                    .setDatabase(0)
                    .setAddress("redis://localhost:" + System.getProperty("embedded-redis.port"))
            }
        return Redisson.create(config)
    }
}

fun createApplicationContext(configuration: Class<*>, includeClientBean: Boolean, vararg properties: Pair<String, Any>) =
    AnnotationConfigApplicationContext()
        .apply {
            environment.propertySources.addFirst(MapPropertySource("testProperties", properties.toMap()))
            if (includeClientBean) register(ClientConfiguration::class.java)
            register(configuration)
            refresh()
        }
