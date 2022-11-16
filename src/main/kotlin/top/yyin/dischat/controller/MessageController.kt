package top.yyin.dischat.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import top.yyin.dischat.data.request.VerifyCodeRequest
import top.yyin.dischat.data.response.BaseResponse
import top.yyin.dischat.service.SmsService

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 2:19 2022/11/13
 */
@Controller
@RequestMapping("/verification")
class MessageController(
    private val smsService: SmsService
){

    @GetMapping("/phone/{phone}")
    fun getAuthCode(@PathVariable phone: String): ResponseEntity<BaseResponse> {
        val  response=smsService.sendCode(phone)
        return when(response.code){
            200 -> ResponseEntity(response,HttpStatus.OK)
            409 -> ResponseEntity(response,HttpStatus.CONFLICT)
            else -> ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/verify")
    fun verifyAuthCode(@RequestBody request:VerifyCodeRequest): ResponseEntity<BaseResponse> {
        val  response=smsService.verifyCode(request.phone,request.code)
        return when(response.code){
            404 -> ResponseEntity(response,HttpStatus.NOT_FOUND)
            200 -> ResponseEntity(response,HttpStatus.OK)
            409 -> ResponseEntity(response,HttpStatus.CONFLICT)
            else -> ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}