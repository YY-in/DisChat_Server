package top.yyin.dischat.security.token

import top.yyin.dischat.service.QiniuCloudService
import top.yyin.dischat.service.QiniuCloudServiceImpl
import org.springframework.beans.factory.annotation.Autowired

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 16:18 2022/10/30
 */
data class ImageToken(
    val upload_token : String
)

data class TokenClaim(
    val name :String,
    val value:String
)

/**
 * @param issuer who issued this token (usually the server)
 * @param audience who is the audience of this token (usually the client),and verify the audience(like normal user or admin)
 * @param expiresIn how long this token is valid
 * @param secret the secret key to sign the token
 */
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)
