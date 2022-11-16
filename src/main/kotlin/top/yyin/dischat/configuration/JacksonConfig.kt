package top.yyin.dischat.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.annotation.Resource


/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 9:44 2022/11/11
 */
@Configuration
class JacksonConfig :InitializingBean{
    @Resource
    var mapper = ObjectMapper()

    override fun afterPropertiesSet() {
        val simpleModule :SimpleModule  = SimpleModule().addSerializer(ObjectId::class.java,ToStringSerializer.instance)
        mapper.registerModule(simpleModule)
    }
}

@Configuration
@ConfigurationProperties(prefix = "aliyun")
class AliyunConfig {
    lateinit var accessKeyId: String
    lateinit var accessKeySecret: String
}