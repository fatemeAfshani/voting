package org.voting.user.unitTests

import io.grpc.Context
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.voting.user.adaptor.api.JwtUtil
import org.voting.user.adaptor.api.grpc.VoterGrpcController
import org.voting.user.adaptor.api.interceptors.UserInterceptor
import org.voting.user.adaptor.api.mapper.UpdateVoterProfileMapper
import org.voting.user.domain.ports.inbound.VoterUseCase
import org.voting.user.domain.user.EducationLevels
import org.voting.user.domain.user.Genders
import org.voting.user.domain.user.MaritalStatuses
import org.voting.user.domain.user.Roles
import org.voting.user.domain.voter.VoterModel
import user.User
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.mockito.Mockito.`when` as whenever

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
        val userId = "random"
        val voter = VoterModel(id = "voter-1", telegramId = telegramId, userId = userId)
        whenever(voterUseCase.loginWithTelegram(telegramId)).thenReturn(voter)
        whenever(jwtUtil.generateToken(userId, "VOTER")).thenReturn("jwt-token")

        val request = User.TelegramLoginRequest.newBuilder().setTelegramId(telegramId).build()

        val resp = controller.loginWithTelegram(request)

        assertNotNull(resp)
        assertEquals("random", resp.id)
        assertEquals("jwt-token", resp.token)
        verify(voterUseCase).loginWithTelegram(telegramId)
        verify(jwtUtil).generateToken(userId, "VOTER")
    }

    @Test
    fun `updateProfile with all fields updates successfully`(): Unit = runBlocking {
        val userId = "user-123"
        val userRole = Roles.VOTER.toString()
        val context = Context.current().withValues(
            UserInterceptor.USER_ID_KEY,
            userId,
            UserInterceptor.ROLE_KEY,
            userRole
        )
        val previousContext = context.attach()

        try {
            val updatedVoter = VoterModel(
                id = userId,
                telegramId = "tg-123",
                city = "New York",
                gender = Genders.MALE,
                age = 25,
                job = "Software Engineer",
                educationLevel = EducationLevels.BACHELOR,
                fieldOfStudy = "Computer Science",
                maritalStatus = MaritalStatuses.SINGLE
            )

            val request = User.UpdateVoterProfileRequest.newBuilder()
                .setCity("New York")
                .setGender(User.Genders.MALE)
                .setAge(25)
                .setJob("Software Engineer")
                .setEducationLevel(User.EducationLevels.BACHELOR)
                .setFieldOfStudy("Computer Science")
                .setMaritalStatus(User.MaritalStatuses.SINGLE)
                .build()

            whenever(
                voterUseCase.updateProfile(UpdateVoterProfileMapper.mapper.protoToDto(request, userId, userRole))
            ).thenReturn(updatedVoter)

            val response = controller.updateProfile(request)

            assertNotNull(response)
            verify(voterUseCase).updateProfile(UpdateVoterProfileMapper.mapper.protoToDto(request, userId, userRole))
        } finally {
            context.detach(previousContext)
        }
    }

    @Test
    fun `updateProfile with partial fields updates successfully`(): Unit = runBlocking {
        val userId = "user-456"
        val userRole = Roles.VOTER.toString()
        val context = Context.current().withValues(
            UserInterceptor.USER_ID_KEY,
            userId,
            UserInterceptor.ROLE_KEY,
            userRole
        )
        val previousContext = context.attach()

        try {
            val updatedVoter = VoterModel(
                id = userId,
                telegramId = "tg-456",
                city = "London",
                age = 30
            )

            val request = User.UpdateVoterProfileRequest.newBuilder()
                .setCity("London")
                .setAge(30)
                .build()

            whenever(
                voterUseCase.updateProfile(
                    UpdateVoterProfileMapper.mapper.protoToDto(request, userId, userRole)
                )
            ).thenReturn(updatedVoter)

            val response = controller.updateProfile(request)

            assertNotNull(response)
            verify(voterUseCase).updateProfile(
                UpdateVoterProfileMapper.mapper.protoToDto(request, userId, userRole)
            )
        } finally {
            context.detach(previousContext)
        }
    }
}
