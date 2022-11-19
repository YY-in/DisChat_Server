package top.yyin.dischat.service

import com.aliyun.dm20151123.Client
import com.aliyun.dm20151123.models.SingleSendMailRequest
import com.aliyun.tea.*
import com.aliyun.teaopenapi.models.Config
import com.aliyun.teautil.Common
import com.aliyun.teautil.models.RuntimeOptions
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import top.yyin.dischat.configuration.AliyunConfig
import top.yyin.dischat.data.response.BaseResponse
import top.yyin.dischat.utils.RedisUtils
import javax.annotation.Resource


/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 20:28 2022/11/17
 */
@Service
class EmailService(
    @Autowired val aliyunConfig: AliyunConfig,
) {
    @Resource
    private lateinit var redisUtils: RedisUtils

    fun verifyCode(email: String, code: String): BaseResponse {
        val redisCode = redisUtils.get(email) ?: return BaseResponse(404, "邮箱地址不存在")
        return if (redisCode != code) {
            BaseResponse(409, "验证码错误")
        }else{
            BaseResponse(200, "验证成功")
        }
    }
    fun sendCode(email: String) :BaseResponse{
        val code = (10000..99999).random()
        return when(val message= this.sendEmail(email, code.toString())){
            "\"OK\"" -> {
                redisUtils.set(email, code.toString(), 15)
                BaseResponse(200,"验证码发送成功")}
            else -> BaseResponse(409,message)
        }
    }

    @Throws(Exception::class)
    private  fun createClient(): Client {
        val config = Config() // 必填，您的 AccessKey ID
            .setAccessKeyId(aliyunConfig.accessKeyId) // 必填，您的 AccessKey Secret
            .setAccessKeySecret(aliyunConfig.accessKeySecret)
        // 访问的域名
        config.endpoint = aliyunConfig.dmEndpoint
        return Client(config)
    }


    @Throws(Exception::class)
    private fun sendEmail(email: String, code: String): String{

        val client: Client = this.createClient()
        val singleSendMailRequest = SingleSendMailRequest()
            .setAccountName(aliyunConfig.accountName)
            .setAddressType(aliyunConfig.addressType)
            .setSubject(aliyunConfig.subject)
            .setReplyToAddress(false)
            .setToAddress(email)
            .setHtmlBody("<body>\n" +
                    "\n" +
                    "    &nbsp;\n" +
                    "\n" +
                    "\n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "    <span style=\"font-size:0px\"><span style=\"font-size:0px\">请验证你的邮箱。</span></span>\n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "\n" +
                    "\n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "    \n" +
                    "        <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse\">\n" +
                    "            <tbody>\n" +
                    "            <tr>\n" +
                    "                <td height=\"15\" style=\"line-height:15px\" colspan=\"3\">&nbsp;</td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td width=\"32\" align=\"left\" valign=\"middle\" style=\"height:32px;line-height:0px\">\n" +
                    "                    <div><img height=\"50\" src=\"https://qiniu.yyin.top/dischat_logo.png\" style=\"border:0\"></div>\n" +
                    "                </td>\n" +
                    "                <td width=\"15\" style=\"display:block;width:15px\">&nbsp;&nbsp;&nbsp;</td>\n" +
                    "                <td width=\"100%\"><span></span></td>\n" +
                    "            </tr>\n" +
                    "            <tr style=\"border-bottom:solid 1px #e5e5e5\">\n" +
                    "                <td height=\"15\" style=\"line-height:15px\" colspan=\"3\">&nbsp;</td>\n" +
                    "            </tr>\n" +
                    "            </tbody>\n" +
                    "        </table>\n" +
                    "    \n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "\n" +
                    "\n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "    \n" +
                    "        <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse\">\n" +
                    "            <tbody>\n" +
                    "            <tr>\n" +
                    "                <td height=\"28\" style=\"line-height:28px\">&nbsp;</td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td>\n" +
                    "                    <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse\">\n" +
                    "                        <tbody>\n" +
                    "                        <tr>\n" +
                    "                            <td style=\"font-size:11px;font-family:LucidaGrande,tahoma,verdana,arial,sans-serif;padding-bottom:10px\">\n" +
                    "                                <span style=\"font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;font-size:16px;line-height:21px;color:#141823\">你好呀：</span>\n" +
                    "                            </td>\n" +
                    "                        </tr>\n" +
                    "                        <tr>\n" +
                    "                            <td style=\"font-size:11px;font-family:LucidaGrande,tahoma,verdana,arial,sans-serif;padding-top:10px;padding-bottom:10px\">\n" +
                    "                                <span style=\"font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;font-size:16px;line-height:21px;color:#141823\">请输入以下验证码来为你的新 DisChat 帐户验证邮箱。</span>\n" +
                    "                            </td>\n" +
                    "                        </tr>\n" +
                    "                        <tr>\n" +
                    "                            <td style=\"font-size:11px;font-family:LucidaGrande,tahoma,verdana,arial,sans-serif;padding-top:10px;padding-bottom:10px\">\n" +
                    "                                <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse\">\n" +
                    "                                    <tbody>\n" +
                    "                                    <tr>\n" +
                    "                                        <td style=\"padding-bottom:4px\"><span style=\"font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;font-size:14px;line-height:19px;color:#1c1e21\">验证码</span>\n" +
                    "                                        </td>\n" +
                    "                                    </tr>\n" +
                    "                                    <tr>\n" +
                    "                                        <td>\n" +
                    "\n" +
                    "                                            <div style=\"font-size:24px;font-weight:400;background:#f1f4f7;color:#1c2b33;border-radius:4px;letter-spacing:2px;padding:16px\" >\n" + code +
                    "                                            </div>\n" +
                    "                                        </td>\n" +
                    "                                    </tr>\n" +
                    "                                    </tbody>\n" +
                    "                                </table>\n" +
                    "                            </td>\n" +
                    "                        </tr>\n" +
                    "                        <tr>\n" +
                    "                            <td style=\"font-size:11px;font-family:LucidaGrande,tahoma,verdana,arial,sans-serif;padding-top:10px\">\n" +
                    "                                <span style=\"font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;font-size:16px;line-height:21px;color:#141823\">如果并未申请验证码，你可以忽略这封邮件。</span>\n" +
                    "                            </td>\n" +
                    "                        </tr>\n" +
                    "                        </tbody>\n" +
                    "                    </table>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td height=\"28\" style=\"line-height:28px\">&nbsp;</td>\n" +
                    "            </tr>\n" +
                    "            </tbody>\n" +
                    "        </table>\n" +
                    "    \n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "\n" +
                    "\n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "    \n" +
                    "        <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"border-collapse:collapse\">\n" +
                    "            <tbody>\n" +
                    "            <tr style=\"border-top:solid 1px #e5e5e5\">\n" +
                    "                <td height=\"19\" style=\"line-height:19px\">&nbsp;</td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td style=\"font-family:Helvetica Neue,Helvetica,Lucida Grande,tahoma,verdana,arial,sans-serif;font-size:11px;color:#aaaaaa;line-height:16px\">\n" +
                    "                    这封邮件已按你的请求发送到 <a href=\"?&amp;cs=wh&amp;v=b&amp;to=\" style=\"color:#3b5998;text-decoration:none\" target=\"_blank\" rel=\"noreferrer\">$email</a>\n" +
                    "                    <br>DisChat Platforms, Inc., Attention: Community Support, NanJing, JiangSu, 210000, China\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "            </tbody>\n" +
                    "        </table>\n" +
                    "    \n" +
                    "    &nbsp;&nbsp;&nbsp;\n" +
                    "\n" +
                    "\n" +
                    "    &nbsp;\n" +
                    "\n" +
                    "\n" +
                    "</body>")
        val runtime = RuntimeOptions()
        return try {
            // 复制代码运行请自行打印 API 的返回值
            val response = client.singleSendMailWithOptions(singleSendMailRequest, runtime)
            return "200"
        } catch (error: TeaException) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message)
        } catch (_error: java.lang.Exception) {
            val error = TeaException(_error.message, _error)
            // 如有需要，请打印 error
            Common.assertAsString(error.message)
        }
    }
}