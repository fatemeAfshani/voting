package org.voting.user.unitTests

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.ResponseEntity
import org.voting.user.adaptor.api.CreatorController
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.api.dto.CreatorLoginResponse
import org.voting.user.domain.creator.CreatorModel
import org.voting.user.domain.creator.CreatorService
import org.voting.user.domain.creator.dto.RegisterDto
import org.voting.user.domain.creator.dto.TelegramLoginRequest
import org.voting.user.domain.user.Roles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class CreatorControllerTest {

    @InjectMocks
    private lateinit var controller: CreatorController

    @Mock
    private lateinit var service: CreatorService

    @Mock
    private lateinit var jwtUtil: JwtUtil

    @Mock
    private lateinit var  meterRegistry: MeterRegistry

    @Mock
    private lateinit var counter: Counter

    @Test
    fun `should register creator successfully`() {
        val request = RegisterDto("1234567890", "password123", "testUser")
        val creator = CreatorModel(id = "1", phone = "1234567890", password = "password123", telegramId = "testUser")
        val token = "mockToken"

        `when`(service.register(request)).thenReturn(creator)
        `when`(meterRegistry.counter("register_counter", *arrayOf("register", "/api/v1/creator/register")))
            .thenReturn(counter)
        `when`(jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)).thenReturn(token)
        val response = controller.registerCreator(request)

        assertNotNull(response.body)
        assertEquals(token, response.body!!.token)
        verify(service).register(request)
        verify(counter).increment()

    }

    @Test
    fun `should login creator and return token`() {
        val request = RegisterDto("1234567890", "password123")
        val creator = CreatorModel(id = "1", phone = "1234567890", password = "password123", telegramId = "testUser")
        val token = "mockToken"

        `when`(service.login(request)).thenReturn(creator)
        `when`(jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)).thenReturn(token)

        val response: ResponseEntity<CreatorLoginResponse> = controller.loginCreator(request)

        assertNotNull(response.body)
        assertEquals(token, response.body!!.token)
    }

    @Test
    fun `should login creator with telegramId and return token`() {
        val telegramId = "sampleUser"
        val creator = CreatorModel(id = "1", phone = "1234567890", password = "password123", telegramId = "testUser")
        val token = "mockToken"
        val request = TelegramLoginRequest(telegramId)

        `when`(service.loginWithTelegram(telegramId)).thenReturn(creator)
        `when`(jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)).thenReturn(token)

        val response: ResponseEntity<CreatorLoginResponse> = controller.loginWithTelegram(request)

        assertNotNull(response.body)
        assertEquals(token, response.body!!.token)
    }
}
