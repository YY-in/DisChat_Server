package top.yyin.dischat.service

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import top.yyin.dischat.domain.security.hashing.SaltedHash
import java.security.SecureRandom

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 19:07 2022/11/9
 */

interface HashingService {
    fun generateSaltedHash(value:String, saltLength:Int =32): SaltedHash
    fun verify(value : String, saltedHash : SaltedHash):Boolean
}

@Service
class SHA256HashingService :HashingService {
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        // A Secure Way to generate a random salt
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        // Hash the value with the salt
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(
            hash=hash,
            salt=saltAsHex
        )
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt+value) == saltedHash.hash
    }
}