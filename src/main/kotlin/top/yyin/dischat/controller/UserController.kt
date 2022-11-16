package top.yyin.dischat.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import top.yyin.dischat.data.request.LoginEmailRequest
import top.yyin.dischat.data.request.LoginPhoneRequest
import top.yyin.dischat.data.request.UserPrivateRequest
import top.yyin.dischat.data.response.AuthToken
import top.yyin.dischat.data.response.BaseResponse
import top.yyin.dischat.data.response.Response
import top.yyin.dischat.domain.user.UserPrivate
import top.yyin.dischat.repository.user.UserPrivateRepository
import top.yyin.dischat.service.SHA256HashingService
import top.yyin.dischat.service.user.UserService
import javax.servlet.http.HttpServletRequest

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 21:20 2022/11/9
 */
@RestController
@RequestMapping("/users")
class UserController (
    private val userService: UserService,
    private val hashingService: SHA256HashingService,
    ) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/authenticate")
    fun authenticate(request: HttpServletRequest):ResponseEntity<BaseResponse>{
        val token = request.getHeader("Authorization")
        logger.info("$token\n")
        val  response=userService.authenticate(token)
        return when(response.code){
            200 -> ResponseEntity(response,HttpStatus.OK)
            401 -> ResponseEntity(response,HttpStatus.UNAUTHORIZED)
            else -> ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody request: UserPrivateRequest):UserPrivate{

        val saltedHash = hashingService.generateSaltedHash(request.password)

        val user = UserPrivate(
            password = saltedHash.hash,
            salt = saltedHash.salt,
            bot = false,
            bio = null,
            username = request.username,
            avatarUrl = request.avatarUrl,
            locale = request.locale,
            email = request.email,
            phone = request.phone,
            verified = true
        )
        userService.registerUser(user)
        return  user
    }

    @PostMapping("/login/phone")
    fun loginByPhone(@RequestBody request: LoginPhoneRequest): ResponseEntity<Response<AuthToken>> {
        val response =  userService.loginByPhone(request.phone,request.password)
        return when(response.code){
            404 -> ResponseEntity(response,HttpStatus.NOT_FOUND)
            409 -> ResponseEntity(response,HttpStatus.CONFLICT)
            200 -> ResponseEntity(response,HttpStatus.OK)
            else -> ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/login/email")
    fun loginByEmail(@RequestBody request: LoginEmailRequest): ResponseEntity<Response<AuthToken>> {
        val response =  userService.loginByEmail(request.email,request.password)
        return when(response.code){
            404 -> ResponseEntity(response,HttpStatus.NOT_FOUND)
            409 -> ResponseEntity(response,HttpStatus.CONFLICT)
            200 -> ResponseEntity(response,HttpStatus.OK)
            else -> ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}