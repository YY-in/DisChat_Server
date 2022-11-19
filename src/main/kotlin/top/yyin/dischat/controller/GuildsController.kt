package top.yyin.dischat.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 16:33 2022/11/16
 */
@Controller
@RequestMapping("/guilds")
class GuildsController{
    @GetMapping("/{guidId}")
    fun getGuildById(@PathVariable guidId: String){

    }
}