package top.yyin.dischat.utils

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 15:21 2022/11/13
 */
@Component
class RedisUtils {
    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    // =============================common============================
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    fun expire(key: String, time: Long): Boolean {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    fun getExpire(key: String): Long {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS)
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    fun hasKey(key: String): Boolean {
        try {
            return redisTemplate.hasKey(key)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    fun del(vararg key: String) {
        if (key.isNotEmpty()) {
            if (key.size == 1) {
                redisTemplate.delete(key[0])
            } else {
                redisTemplate.delete(key.toList())
            }
        }
    }
    // ============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    operator fun get(key: String?): Any? {
        return if (key == null) null else redisTemplate.opsForValue().get(key)
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    operator fun set(key: String, value: Any): Boolean {
        try {
            redisTemplate.opsForValue().set(key, value)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }
    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    operator fun set(key: String, value: Any, time: Long): Boolean {
        return try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES)
            } else {
                set(key, value)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

}