//package org.voting.usermanagement.integrationTests
//
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
//import org.junit.jupiter.api.Order
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestMethodOrder
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.http.MediaType
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
//import org.testcontainers.containers.MongoDBContainer
//import org.testcontainers.junit.jupiter.Container
//import org.testcontainers.junit.jupiter.Testcontainers
//import org.voting.pollmanagement.domain.ports.outbound.persistance.PollRepository
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@TestMethodOrder(OrderAnnotation::class)
//@ActiveProfiles("test")
//@Testcontainers
//class CreatorIntegrationTest {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @Autowired
//    var creatorRepository: PollRepository? = null
//
//    companion object {
//        @Container
//        val mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:latest")
//            .withExposedPorts(27017)
//
//        init {
//            mongoDBContainer.start()
//            System.setProperty("spring.data.mongodb.uri", mongoDBContainer.replicaSetUrl)
//        }
//    }
//
//    private val registerJson = """
//        {
//            "phone": "1234567890",
//            "password": "password123",
//            "userName": "testUser"
//        }
//    """.trimIndent()
//
//    private val loginJson = """
//        {
//            "phone": "1234567890",
//            "password": "password123"
//        }
//    """.trimIndent()
//
//    @Test
//    @Order(1)
//    fun `should register creator successfully`() {
//        mockMvc.perform(
//            post("/api/v1/creator/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(registerJson)
//        )
//            .andExpect(status().isOk)
//            .andExpect(content().string("user has been registered successfully."))
//    }
//
//    @Test
//    @Order(2)
//    fun `should login creator and return token`() {
//        mockMvc.perform(
//            post("/api/v1/creator/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(loginJson)
//        )
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.token").exists())
//    }
//}
