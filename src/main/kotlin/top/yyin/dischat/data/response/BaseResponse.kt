package top.yyin.dischat.data.response

import jdk.jfr.DataAmount
import lombok.Data
import org.springframework.http.HttpStatus

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 12:31 2022/11/10
 */

data class BaseResponse(
    val code :Int,
    val message: String
)
data class Response<T>(
    val code: Int = 200,
    val data: T? = null,
    val message: String = "请求成功",
    val error: String ?=null
)

