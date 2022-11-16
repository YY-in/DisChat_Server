package top.yyin.dischat.repository.user

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import top.yyin.dischat.domain.user.UserPrivate

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 20:18 2022/11/9
 */
@Repository
interface UserPrivateRepository : MongoRepository<UserPrivate,ObjectId> {
    // 查询并返回用户信息
    fun findUserPrivateByUsername(username: String): UserPrivate
    fun findUserPrivatesByEmail(email: String): UserPrivate
    fun findUserPrivatesByPhone(phone: String): UserPrivate

    fun existsByPhoneOrEmail(phone: String?, email: String?): Boolean

}