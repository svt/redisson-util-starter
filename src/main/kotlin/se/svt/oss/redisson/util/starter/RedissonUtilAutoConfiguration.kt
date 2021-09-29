// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.util.starter

import org.redisson.api.RedissonClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import se.svt.oss.redisson.util.starter.config.RedissonUtilProperties
import se.svt.oss.redisson.util.starter.lock.RedissonLockService
import se.svt.oss.redisson.util.starter.queue.QueueItem
import se.svt.oss.redisson.util.starter.queue.RedissonLibQueue

@EnableConfigurationProperties(RedissonUtilProperties::class)
@Configuration
class RedissonUtilAutoConfiguration {

    @Bean
    @ConditionalOnProperty("redisson-util.lock.name-prefix")
    @ConditionalOnBean(RedissonClient::class)
    fun redissonLockService(redissonClient: RedissonClient, redissonUtilProperties: RedissonUtilProperties) =
        RedissonLockService(redissonClient, redissonUtilProperties)

    @Bean
    @ConditionalOnProperty("redisson-util.queue.name")
    @ConditionalOnBean(RedissonClient::class)
    fun redissonPriorityQueue(redisson: RedissonClient, redissonUtilProperties: RedissonUtilProperties): RedissonLibQueue {
        val priorityQueue = redisson.getPriorityBlockingQueue<QueueItem>(redissonUtilProperties.queue.name)
        return RedissonLibQueue(priorityQueue)
    }
}
