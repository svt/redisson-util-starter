// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.util.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("redisson-util")
class RedissonUtilProperties {
    var lock = LockProperties()

    var queue = QueueProperties()

    override fun toString(): String {
        return "RedissonUtilProperties(lock=$lock, queue=$queue)"
    }
}

class LockProperties {
    var leaseTime = Duration.ofMillis(-1)
    var waitTime = Duration.ZERO
    var namePrefix: String? = null

    override fun toString(): String {
        return "RedissonLockProperties(leaseTime=$leaseTime, waitTime=$waitTime, namePrefix='$namePrefix')"
    }
}

class QueueProperties {
    var name: String? = null

    override fun toString(): String {
        return "RedissonQueueProperties(name='$name')"
    }
}
