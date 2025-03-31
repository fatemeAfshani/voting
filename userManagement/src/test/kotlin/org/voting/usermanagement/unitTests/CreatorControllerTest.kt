package org.voting.usermanagement.unitTests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.ResponseEntity
import org.voting.usermanagement.adaptor.api.CreatorController
import org.voting.usermanagement.adaptor.api.JwtUtil
import org.voting.usermanagement.adaptor.api.dto.CreatorLoginResponse
import org.voting.usermanagement.domain.creator.CreatorModel
import org.voting.usermanagement.domain.creator.CreatorService
import org.voting.usermanagement.domain.creator.dto.RegisterDto
import org.voting.usermanagement.domain.user.Roles
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

    @Test
    fun `should register creator successfully`() {
        val request = RegisterDto("1234567890", "password123", "testUser")

        val response = controller.registerCreator(request)

        assertEquals(ResponseEntity.ok("user has been registered successfully."), response)
    }

    @Test
    fun `should login creator and return token`() {
        val request = RegisterDto("1234567890", "password123")
        val creator = CreatorModel(id = "1", phone = "1234567890", password = "password123", userName = "testUser")
        val token = "mockToken"

        `when`(service.login(request)).thenReturn(creator)
        `when`(jwtUtil.generateToken(creator.id!!, Roles.CREATOR.name)).thenReturn(token)

        val response: ResponseEntity<CreatorLoginResponse> = controller.loginCreator(request)

        assertNotNull(response.body)
        assertEquals(token, response.body!!.token)
    }
}
