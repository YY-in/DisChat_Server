package com.example.qiniutest.service

import com.qiniu.common.QiniuException
import com.qiniu.http.Response
import com.qiniu.storage.BucketManager
import com.qiniu.storage.UploadManager
import com.qiniu.util.Auth
import com.qiniu.util.StringMap
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 22:32 2022/10/29
 */
interface QiniuCloudService {
    @Throws(QiniuException::class)
    fun uploadFile(file: File, filename: String): String

    @Throws(QiniuException::class)
    fun uploadFileStream(inputStream: InputStream, filename: String): String

    @Throws(QiniuException::class)
    fun remove(key: String): String
}

@Service
class QiniuCloudServiceImpl: QiniuCloudService, InitializingBean {
    @Autowired
    lateinit var uploaderManager: UploadManager

    @Autowired
    lateinit var bucketManager: BucketManager

    @Autowired
    lateinit var auth: Auth

    @Value("\${qiniu.bucket}")
    lateinit var bucket: String

    @Value("\${qiniu.domain}")
    lateinit var domain: String

    lateinit var putPolicy: StringMap

    override fun uploadFile(file: File, filename: String): String {
        var response: Response? = uploaderManager.put(file, filename, getUploadToken())
        var retry = 0
        while (response?.needRetry() == true && retry < 3) {
            response = uploaderManager.put(file, filename, getUploadToken());
            retry++
        }
        while (response?.statusCode == 200) {
            return "https://$domain/$filename"
        }
        return "上传失败"
    }

    override fun uploadFileStream(inputStream: InputStream, filename: String): String {
        var response: Response = uploaderManager.put(inputStream, filename, getUploadToken(), null, null)
        var retry = 0
        while (response.needRetry() && retry < 3) {
            response = uploaderManager.put(inputStream, filename, getUploadToken(), null, null);
            retry++
        }
        return if (response.statusCode == 200) "https://$domain/$filename" else "上传失败"
    }

    override fun remove(key: String): String {
        var response: Response = bucketManager.delete(bucket, key)
        var retry = 0
        while (response.needRetry() && retry < 3) {
            response = bucketManager.delete(bucket, key)
            retry++
        }
        return if (response.statusCode == 200) "删除成功" else "删除失败"
    }

    override fun afterPropertiesSet() {
        putPolicy = StringMap()
        putPolicy.put(
            "returnBody",
            "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":\${imageInfo.height}}"
        )
    }

    fun getUploadToken(): String {
        return auth.uploadToken(bucket, null, 3600, putPolicy)
    }
}
