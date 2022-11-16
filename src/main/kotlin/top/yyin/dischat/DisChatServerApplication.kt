package top.yyin.dischat

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class DisChatServerApplication

fun main(args: Array<String>) {
    runApplication<DisChatServerApplication>(*args)
}
