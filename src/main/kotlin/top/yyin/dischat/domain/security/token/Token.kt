package top.yyin.dischat.domain.security.token

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

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
@Component
data class TokenConfig(
    @Value("\${jwt.issuer}")var issuer: String,
    @Value("\${jwt.audience}") var  audience: String,
    @Value("\${jwt.expireTime}") var expiresIn: Long,
    @Value("\${jwt.secret}") var secret: String
)
