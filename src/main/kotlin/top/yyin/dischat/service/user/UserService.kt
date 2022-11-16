package top.yyin.dischat.service.user

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import top.yyin.dischat.data.response.AuthToken
import top.yyin.dischat.data.response.BaseResponse
import top.yyin.dischat.data.response.Response
import top.yyin.dischat.domain.security.hashing.SaltedHash
import top.yyin.dischat.domain.security.token.TokenClaim
import top.yyin.dischat.domain.security.token.TokenConfig
import top.yyin.dischat.domain.user.UserPrivate
import top.yyin.dischat.repository.user.UserPrivateRepository
import top.yyin.dischat.service.JwtTokenService
import top.yyin.dischat.service.SHA256HashingService
import top.yyin.dischat.service.TokenService

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 12:37 2022/11/10
 */
interface UserService {
    fun registerUser(user: UserPrivate): UserPrivate
    fun loginByEmail(email:String,password:String): Response<AuthToken>
    fun loginByPhone(phone:String,password:String): Response<AuthToken>
    fun authenticate(token:String):BaseResponse
}

@Service
class UserServiceImpl(
    private val userPrivateRepository: UserPrivateRepository,
    private val hashingService: SHA256HashingService,
    private val tokenService: JwtTokenService,
) : UserService {

    override fun registerUser(user: UserPrivate): UserPrivate =
        userPrivateRepository.save(user)

    override fun loginByEmail(email: String, password: String): Response<AuthToken> {
        val user = userPrivateRepository.findUserPrivatesByEmail(email) ?: return Response(HttpStatus.NOT_FOUND.value(),AuthToken(),message = "该邮箱的用户不存在")
        return validation(user,password)
    }

    override fun loginByPhone(phone: String, password: String): Response<AuthToken> {
        val user = userPrivateRepository.findUserPrivatesByPhone(phone) ?: return Response(HttpStatus.NOT_FOUND.value(),AuthToken(),message = "该电话的用户不存在")
        return validation(user,password)
    }


    fun validation(user:UserPrivate,password:String): Response<AuthToken>{
        val isValidPassword=hashingService.verify(
            value = password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            ))
        if (!isValidPassword) return Response(HttpStatus.CONFLICT.value(),AuthToken(),error = "登录失败")

        val token = tokenService.generate(
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )
        return Response(HttpStatus.OK.value(),AuthToken(token),"登录成功")
    }

    override fun authenticate(token: String): BaseResponse{

        return when(tokenService.validateToken(token)){
            null  -> BaseResponse(HttpStatus.UNAUTHORIZED.value(),"token验证失败")
            else -> BaseResponse(HttpStatus.OK.value(),token.replace("Bearer ",""))
        }
    }

}