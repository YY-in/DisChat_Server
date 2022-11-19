package top.yyin.dischat.domain.chat

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 16:38 2022/11/16
 */

@Document(collection = "guilds")
data class Guild(
    @MongoId val id: ObjectId = ObjectId(),
    val name: String,
    val icon: String?,
    val banner: String? = null,
    val permissions: Long,
    val premium_tier: Int,
    val premium_subscription_count: Int? = null
)

