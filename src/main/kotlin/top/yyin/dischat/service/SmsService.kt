package top.yyin.dischat.service

import com.aliyun.dysmsapi20170525.Client
import com.aliyun.dysmsapi20170525.models.SendSmsRequest
import com.aliyun.tea.TeaException
import com.aliyun.teaopenapi.models.Config
import com.aliyun.teautil.Common
import com.aliyun.teautil.models.RuntimeOptions
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.yyin.dischat.configuration.AliyunConfig
import top.yyin.dischat.data.response.BaseResponse
import top.yyin.dischat.utils.RedisUtils
import javax.annotation.Resource


@Service
class SmsService(
    @Autowired  val aliyunConfig: AliyunConfig
){

    @Resource
    private lateinit var redisUtils: RedisUtils

    fun verifyCode(phone: String, code: String): BaseResponse {
        val redisCode = redisUtils.get(phone) ?: return BaseResponse(404, "电话号码不存在")
        return if (redisCode != code) {
            BaseResponse(409, "验证码错误")
        }else{
            BaseResponse(200, "验证成功")
        }
    }

    fun sendCode(phone: String) :BaseResponse{
        val code = (10000..99999).random()
        return when(val message= this.sendMessage(phone, code.toString())){
            "\"OK\"" -> {
                redisUtils.set(phone, code.toString(), 15)
                BaseResponse(200,"验证码发送成功")}
            else -> BaseResponse(409,message)
        }
    }
    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    @Throws(Exception::class)
    private  fun createClient(): Client {
        val config = Config() // 必填，您的 AccessKey ID
            .setAccessKeyId(aliyunConfig.accessKeyId) // 必填，您的 AccessKey Secret
            .setAccessKeySecret(aliyunConfig.accessKeySecret)
        // 访问的域名
        config.endpoint = aliyunConfig.smsEndpoint
        return Client(config)
    }

    @Throws(Exception::class)
    private fun sendMessage(phone: String, code: String): String{
        val client: Client = this.createClient()
        val sendSmsRequest = SendSmsRequest()
            .setSignName(aliyunConfig.signName)
            .setTemplateCode(aliyunConfig.templateCode)
            .setPhoneNumbers(phone)
            .setTemplateParam("{\"code\":\"$code\"}");
        val runtime = RuntimeOptions()
        return try {
            // 复制代码运行请自行打印 API 的返回值
            val response = client.sendSmsWithOptions(sendSmsRequest, runtime)
            return Gson().toJson(response.body.message)
        } catch (error: TeaException) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message)
        } catch (_error: Exception) {
            val error = TeaException(_error.message, _error)
            // 如有需要，请打印 error
            Common.assertAsString(error.message)
        }
    }
}
