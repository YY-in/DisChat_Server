package top.yyin.dischat.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import top.yyin.dischat.domain.security.token.TokenClaim
import top.yyin.dischat.domain.security.token.TokenConfig
import java.util.*


/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 18:50 2022/11/9
 */
interface TokenService {
    fun generate(
        vararg claims: TokenClaim,
    ):String
    fun validateToken(token:String): Map<String, Claim>?
    fun isNeedUpdate(token:String):Boolean
}

@Service
class JwtTokenService(
    @Value("\${jwt.secret}") val secret:String,
    @Value("\${jwt.tokenPrefix}")  val tokenPrefix:String,
): TokenService {
    private val logger = LoggerFactory.getLogger(JwtTokenService::class.java)
    @Autowired
    lateinit var config:TokenConfig

    override fun generate( vararg claims: TokenClaim): String {
        var token =  JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach {claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }

    override fun validateToken(token: String): Map<String, Claim>? {

        return try {
            JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(tokenPrefix,""))
                .claims
        } catch (e: TokenExpiredException) {
            logger.error("token过期")
            return null
        } catch (e: Exception) {
            logger.error(e.message)
            return null
        }
    }

    override fun isNeedUpdate(token: String): Boolean {
        //获取token过期时间
        var expiresAt: Date? = null
        expiresAt = try {
            JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token.replace(tokenPrefix, ""))
                .expiresAt
        } catch (e: TokenExpiredException) {
            return true
        } catch (e: java.lang.Exception) {
            throw Exception("token验证失败")
        }
        //如果剩余过期时间少于过期时常的一般时 需要更新

        return (expiresAt!!.time - System.currentTimeMillis()) < (config.expiresIn)

    }

}