package pl.pjr.voucher_manager.dao

import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service


@Service
class RedisDaoImpl(private val template: StringRedisTemplate) : RedisDao {
    override fun set(keyName: String, keyValue: String) {
        return template.opsForValue().set(keyName, keyValue)
    }

    override fun get(keyName: String): String? {
        return template.opsForValue().get(keyName)
    }

    override fun scan(): List<String> {
        val l = template.scan(
            ScanOptions.scanOptions()
                .count(100)
                .match("*")
                .build()
            )
            .stream().toList()
        return l
    }
}

interface RedisDao {
    fun set(keyName: String, keyValue: String)

    fun get(keyName: String): String?

    fun scan(): List<String>
}
