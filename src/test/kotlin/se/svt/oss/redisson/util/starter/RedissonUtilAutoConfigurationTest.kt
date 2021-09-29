// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.util.starter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.redisson.api.RPriorityQueue
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import se.svt.oss.junit5.redis.EmbeddedRedisExtension
import se.svt.oss.redisson.util.starter.lock.RedissonLockService
import se.svt.oss.redisson.util.starter.queue.RedissonLibQueue
import se.svt.oss.redisson.util.starter.testutil.createApplicationContext

@ExtendWith(EmbeddedRedisExtension::class)
class RedissonUtilAutoConfigurationTest {

    @Test
    fun `RedissonLockService bean is created if lock name property is set`() {
        val context =
            createApplicationContext(
                RedissonUtilAutoConfiguration::class.java,
                true,
                "redisson-util.lock.name-prefix" to "test-lock"
            )
        context.getBean(RedissonLockService::class.java)
    }

    @Test
    fun `RedissonLockService bean is not created if lock name property is not set`() {
        val context =
            createApplicationContext(
                RedissonUtilAutoConfiguration::class.java,
                true
            )
        Assertions.assertThatThrownBy { context.getBean(RedissonLockService::class.java) }
            .isInstanceOf(NoSuchBeanDefinitionException::class.java)
    }

    @Test
    fun `RedissonLockService bean is not created if there is no redissonClient bean`() {
        val context =
            createApplicationContext(
                RedissonUtilAutoConfiguration::class.java,
                false,
                "redisson-util.lock.name-prefix" to "test-lock"
            )
        Assertions.assertThatThrownBy { context.getBean(RedissonLockService::class.java) }
            .isInstanceOf(NoSuchBeanDefinitionException::class.java)
    }

    @Test
    fun `Redisson queue is created if queue name property is set`() {
        val queueName = "test-queue"
        val context = createApplicationContext(
            RedissonUtilAutoConfiguration::class.java,
            true,
            "redisson-util.queue.name" to queueName
        )
        context.getBean(RedissonLibQueue::class.java)
    }

    @Test
    fun `Redisson queue is not created if queue name property is not set`() {
        val context =
            createApplicationContext(
                RedissonUtilAutoConfiguration::class.java,
                true
            )
        Assertions.assertThatThrownBy { context.getBean(RPriorityQueue::class.java) }
            .isInstanceOf(NoSuchBeanDefinitionException::class.java)
    }

    @Test
    fun `Redisson queue is not created if there is no redissonClient bean`() {
        val queueName = "test-queue"
        val context =
            createApplicationContext(
                RedissonUtilAutoConfiguration::class.java,
                false,
                "redisson-util.queue.name" to queueName
            )
        Assertions.assertThatThrownBy { context.getBean(RPriorityQueue::class.java) }
            .isInstanceOf(NoSuchBeanDefinitionException::class.java)
    }
}
