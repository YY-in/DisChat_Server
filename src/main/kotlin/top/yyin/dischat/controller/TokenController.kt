package top.yyin.dischat.controller

import top.yyin.dischat.domain.security.token.ImageToken
import top.yyin.dischat.service.QiniuCloudServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 15:57 2022/10/30
 */
@RestController
class TokenController {
    @Autowired
    lateinit var qiniuCloudService: QiniuCloudServiceImpl

    @GetMapping("/imageToken")
    fun getImageUploadToken(): ImageToken {
        return ImageToken(upload_token = qiniuCloudService.getUploadToken())
    }

}