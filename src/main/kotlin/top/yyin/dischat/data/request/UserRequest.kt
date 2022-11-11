package top.yyin.dischat.data.request

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

data class LoginPhoneRequest(
    val phone: String,
    val password: String,
)

data class LoginEmailRequest(
    val email: String,
    val password: String,
)