package top.yyin.dischat.data.request

import org.apache.catalina.connector.Response
import org.jetbrains.annotations.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException
import javax.validation.constraints.Email


/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 23:13 2022/11/9
 */
data class UserPrivateRequest(
    val username: String,
    val avatarUrl: String,
    val verified: Boolean,
    val email: String?,
    val phone: String?,
    val password: String,
    val locale:String,
)

@Validated
data class LoginPhoneRequest(
    val phone: String,
    val password: String,
)

data class LoginEmailRequest(
    val email: String,
    val password: String,
)

data class VerifyCodeRequest(
    val phone: String,
    val code: String,
)
data class VerifyEmailCodeRequest(
    val email: String,
    val code: String,
)