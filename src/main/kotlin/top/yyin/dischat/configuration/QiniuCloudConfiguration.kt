package com.example.qiniutest.configuration

import com.qiniu.storage.BucketManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.qiniu.storage.Region
import com.qiniu.storage.UploadManager
import com.qiniu.util.Auth
import com.google.gson.Gson

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 22:09 2022/10/29
 */

@Configuration
class QiniuCloudConfiguration {
    @Value("\${qiniu.accessKey}")
    private val accessKey: String? = null

    @Value("\${qiniu.secretKey}")
    private val secretKey: String? = null

    @Value("\${qiniu.bucket}")
    private val bucket: String? = null

    @Value("\${qiniu.zone}")
    private val zone: String? = null

    /***
     * 配置对应的存储地区
     * 由于Zone的启用，我们使用Region的模式
     */
    @Bean
    fun config(): com.qiniu.storage.Configuration {
        return when (zone) {
            "huadong" -> {
                com.qiniu.storage.Configuration(Region.huadong())
            }
            "huabei" -> {
                com.qiniu.storage.Configuration(Region.huabei())
            }
            "huanan" -> {
                com.qiniu.storage.Configuration(Region.huanan())
            }
            "beimei" -> {
                com.qiniu.storage.Configuration(Region.beimei())
            }
            else -> throw Exception("七牛云区域配置错误")
        }
    }

    /***
     * 获取一个上传工具实例
     */
    @Bean
    fun uploadManager(): UploadManager {
        return UploadManager(config())
    }

    /**
     * 认证信息实例
     */
    @Bean
    fun auth(): Auth {
        return Auth.create(accessKey, secretKey)
    }

    /***
     * 上传空间实例
     */
    @Bean
    fun bucketManager(): BucketManager {
        return BucketManager(auth(), config())
    }

    @Bean
    fun gson(): Gson {
        return Gson()
    }

}