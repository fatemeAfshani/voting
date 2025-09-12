package org.voting.user.unitTests

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import org.mockito.junit.jupiter.MockitoExtension
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.api.grpc.VoterGrpcController
import org.voting.user.domain.ports.inbound.VoterUseCase
import org.voting.user.domain.voter.VoterModel
import user.User
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class VoterGrpcControllerTest {

    @Mock
    private lateinit var voterUseCase: VoterUseCase

    @Mock
    private lateinit var jwtUtil: JwtUtil

    private lateinit var controller: VoterGrpcController

    @BeforeEach
    fun setup() {
        controller = VoterGrpcController(voterUseCase, jwtUtil)
    }

    @Test
    fun `loginWithTelegram returns id and token`(): Unit = runBlocking {
        val telegramId = "tg-123"
        val voter = VoterModel(id = "voter-1", telegramId = telegramId)
        whenever(voterUseCase.loginWithTelegram(telegramId)).thenReturn(voter)
        whenever(jwtUtil.generateToken("voter-1", "VOTER")).thenReturn("jwt-token")

        val request = User.TelegramLoginRequest.newBuilder().setTelegramId(telegramId).build()

        val resp = controller.loginWithTelegram(request)

        assertNotNull(resp)
        assertEquals("voter-1", resp.id)
        assertEquals("jwt-token", resp.token)
        verify(voterUseCase).loginWithTelegram(telegramId)
        verify(jwtUtil).generateToken("voter-1", "VOTER")
    }
}
