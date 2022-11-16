package top.yyin.dischat.controller

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import top.yyin.dischat.domain.user.UserPrivate
import top.yyin.dischat.repository.user.UserPrivateRepository

/**
 * @Author: YinZhihao
 * @Description:
 * @Date: Created in 22:45 2022/11/9
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest (
    private val userRepository: UserPrivateRepository,
    private val restTemplate: TestRestTemplate
){
    @LocalServerPort
    protected var port: Int = 5550

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
    }
    private fun getRootUrl(): String? = "http://localhost:$port/users"

    private fun saveOnePatient() = userRepository.save(
        UserPrivate(
            username = "yyin",
            password = "123456",
            phone = "12345678901",
            email = "1234@em.cn",
            avatarUrl = "https://qiniu.yyin.top/20220709171954.png",
            bio = "",
            bot = false,
            locale = "zh_CN",
            salt = "123456",
            verified = false
        ))

    @Test
    fun `should return all users`() {
        saveOnePatient()

        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }
}