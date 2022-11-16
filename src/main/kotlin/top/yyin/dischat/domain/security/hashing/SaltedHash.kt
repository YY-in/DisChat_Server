package top.yyin.dischat.domain.security.hashing

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 19:08 2022/11/9
 */
data class SaltedHash(
    val hash :String,
    val salt :String
)

