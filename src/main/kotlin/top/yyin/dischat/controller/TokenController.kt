package com.example.qiniutest.controller

import com.example.qiniutest.domain.Token
import com.example.qiniutest.service.QiniuCloudServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
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
    fun getImageUploadToken(): Token {
        return Token(upload_token = qiniuCloudService.getUploadToken())
    }

}