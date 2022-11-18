package top.yyin.dischat.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import top.yyin.dischat.data.request.VerifyCodeRequest
import top.yyin.dischat.data.request.VerifyEmailCodeRequest
import top.yyin.dischat.data.response.BaseResponse
import top.yyin.dischat.service.EmailService
import top.yyin.dischat.service.SmsService

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 10:40 2022/11/18
 */
@Controller
@RequestMapping("/verification")
class EmailController(
    private val emailService: EmailService
){


    @GetMapping("/email/{email}")
    fun getAuthCode(@PathVariable email: String): ResponseEntity<BaseResponse> {
        val  response=emailService.sendCode(email)
        return when(response.code){
            200 -> ResponseEntity(response, HttpStatus.OK)
            409 -> ResponseEntity(response, HttpStatus.CONFLICT)
            else -> ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/email/verify")
    fun verifyAuthCode(@RequestBody request: VerifyEmailCodeRequest): ResponseEntity<BaseResponse> {
        val  response=emailService.verifyCode(request.email,request.code)
        return when(response.code){
            404 -> ResponseEntity(response, HttpStatus.NOT_FOUND)
            200 -> ResponseEntity(response, HttpStatus.OK)
            409 -> ResponseEntity(response, HttpStatus.CONFLICT)
            else -> ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}