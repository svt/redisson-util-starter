// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.util.starter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import se.svt.oss.junit5.redis.EmbeddedRedisExtension
import se.svt.oss.redisson.util.starter.Assertions.assertThat
import se.svt.oss.redisson.util.starter.config.RedissonUtilProperties
import se.svt.oss.redisson.util.starter.testutil.createApplicationContext
import java.time.Duration

@ExtendWith(EmbeddedRedisExtension::class)
class RedissonUtilPropertiesTest {

    @Test
    fun `all redis properties are set correctly`() {
        val lockWaitTime = "10s"
        val lockLeaseTime = "30s"
        val lockName = "the-lock"
        val queueName = "queue"
        val timeout = "5s"
        val context = createApplicationContext(
            OnlyPropertiesConfiguration::class.java,
            true,
            "redisson-util.lock.wait-time" to lockWaitTime,
            "redisson-util.lock.lease-time" to lockLeaseTime,
            "redisson-util.lock.name-prefix" to lockName,
            "redisson-util.queue.name" to queueName,
            "redisson-util.timeout" to timeout
        )

        val redissonUtilProperties = context.getBean(RedissonUtilProperties::class.java)

        System.out.println(redissonUtilProperties)

        assertThat(redissonUtilProperties)
            .isNotNull

        assertThat(redissonUtilProperties.lock)
            .isNotNull
            .hasLeaseTime(Duration.ofSeconds(30))
            .hasWaitTime(Duration.ofSeconds(10))
            .hasNamePrefix(lockName)

        assertThat(redissonUtilProperties.queue)
            .isNotNull
            .hasName(queueName)
    }

    @Test
    fun `not all redis properties are set`() {
        val context = createApplicationContext(
            OnlyPropertiesConfiguration::class.java,
            true
        )

        val redissonUtilProperties = context.getBean(RedissonUtilProperties::class.java)

        System.out.println(redissonUtilProperties)

        assertThat(redissonUtilProperties)
            .isNotNull

        assertThat(redissonUtilProperties.lock)
            .isNotNull
            .hasNamePrefix(null)
            .hasLeaseTime(Duration.ofMillis(-1))
            .hasWaitTime(Duration.ZERO)

        assertThat(redissonUtilProperties.queue)
            .isNotNull
            .hasName(null)
    }

    @EnableConfigurationProperties(RedissonUtilProperties::class)
    @Configuration
    class OnlyPropertiesConfiguration
}
