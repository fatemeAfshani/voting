package org.voting.user.integrationTests

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.voting.user.adaptor.exception.Errors
import org.voting.user.adaptor.exception.ForbiddenException
import org.voting.user.adaptor.exception.NotFoundException
import org.voting.user.adaptor.persistance.repository.MongoVoterRepository
import org.voting.user.domain.ports.inbound.VoterUseCase
import org.voting.user.domain.user.EducationLevels
import org.voting.user.domain.user.Genders
import org.voting.user.domain.user.MaritalStatuses
import org.voting.user.domain.user.Roles
import org.voting.user.domain.voter.dto.UpdateProfileDto
import user.User
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class VoterIntegrationTest {

    @Autowired
    private lateinit var voterUseCase: VoterUseCase

    @Autowired
    private lateinit var mongoVoterRepository: MongoVoterRepository

    companion object {
        @Container
        val mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:latest").withExposedPorts(27017)

        init {
            mongoDBContainer.start()
            System.setProperty("spring.data.mongodb.uri", mongoDBContainer.replicaSetUrl)
        }
    }

    @BeforeEach
    fun setup() {
        mongoVoterRepository.deleteAll()
    }

    @Test
    fun `loginWithTelegram creates voter and returns token`(): Unit = runBlocking {
        val telegramId = "tg-int-1"
        val request = User.TelegramLoginRequest.newBuilder().setTelegramId(telegramId).build()

        val voter = voterUseCase.loginWithTelegram(request.telegramId)

        assertNotNull(voter.id)
        val saved = mongoVoterRepository.findByTelegramId(telegramId)
        assertNotNull(saved)
        assertEquals(telegramId, saved.telegramId)
    }

    @Test
    fun `loginWithTelegram fetches existing voter`(): Unit = runBlocking {
        val telegramId = "tg-int-2"
        mongoVoterRepository.save(org.voting.user.adaptor.persistance.entity.Voter(telegramId = telegramId))

        val voter = voterUseCase.loginWithTelegram(telegramId)

        assertNotNull(voter.id)
        val saved = mongoVoterRepository.findByTelegramId(telegramId)
        assertNotNull(saved)
        assertEquals(saved.id, voter.id)
    }


    @Test
    fun `updateProfile with all fields updates successfully`(): Unit = runBlocking {
        val telegramId = "tg-profile-1"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            city = "New York",
            gender = Genders.MALE,
            age = 25,
            job = "Software Engineer",
            educationLevel = EducationLevels.BACHELOR,
            fieldOfStudy = "Computer Science",
            maritalStatus = MaritalStatuses.SINGLE,
            userId = voter.id,
            userRole = Roles.VOTER
        )

        val updatedVoter = voterUseCase.updateProfile(updateDto)

        assertEquals("New York", updatedVoter.city)
        assertEquals(Genders.MALE, updatedVoter.gender)
        assertEquals(25, updatedVoter.age)
        assertEquals("Software Engineer", updatedVoter.job)
        assertEquals(EducationLevels.BACHELOR, updatedVoter.educationLevel)
        assertEquals("Computer Science", updatedVoter.fieldOfStudy)
        assertEquals(MaritalStatuses.SINGLE, updatedVoter.maritalStatus)
    }

    @Test
    fun `updateProfile with partial fields updates successfully`(): Unit = runBlocking {
        val telegramId = "tg-profile-2"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            city = "London",
            age = 30,
            userId = voter.id,
            userRole = Roles.VOTER
        )

        val updatedVoter = voterUseCase.updateProfile(updateDto)

        assertEquals("London", updatedVoter.city)
        assertEquals(30, updatedVoter.age)
        assertEquals(null, updatedVoter.gender)
        assertEquals(null, updatedVoter.job)
        assertEquals(null, updatedVoter.educationLevel)
        assertEquals(null, updatedVoter.fieldOfStudy)
        assertEquals(null, updatedVoter.maritalStatus)
    }

    @Test
    fun `updateProfile with empty fields updates successfully`(): Unit = runBlocking {
        val telegramId = "tg-profile-3"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            userId = voter.id,
            userRole = Roles.VOTER
        )

        val updatedVoter = voterUseCase.updateProfile(updateDto)

        assertEquals(voter.id, updatedVoter.id)
        assertEquals(telegramId, updatedVoter.telegramId)
    }

    @Test
    fun `updateProfile throws ForbiddenException when user role is not VOTER`(): Unit = runBlocking {
        val telegramId = "tg-profile-4"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            city = "Paris",
            userId = voter.id,
            userRole = Roles.CREATOR
        )

        val exception = assertThrows<ForbiddenException> {
            voterUseCase.updateProfile(updateDto)
        }

        assertEquals(Errors.ErrorCodes.FORBIDDEN.name, exception.message)
    }

    @Test
    fun `updateProfile throws ForbiddenException when userId is null`(): Unit = runBlocking {
        val updateDto = UpdateProfileDto(
            city = "Paris",
            userId = null,
            userRole = Roles.VOTER
        )

        val exception = assertThrows<ForbiddenException> {
            voterUseCase.updateProfile(updateDto)
        }

        assertEquals(Errors.ErrorCodes.FORBIDDEN.name, exception.message)
    }

    @Test
    fun `updateProfile throws NotFoundException when user not found`(): Unit = runBlocking {
        val updateDto = UpdateProfileDto(
            city = "Paris",
            userId = "non-existent-user",
            userRole = Roles.VOTER
        )

        val exception = assertThrows<NotFoundException> {
            voterUseCase.updateProfile(updateDto)
        }

        assertEquals(Errors.ErrorCodes.USER_NOT_FOUND.name, exception.message)
    }

    @Test
    fun `updateProfile with invalid age throws IllegalArgumentException`(): Unit = runBlocking {
        val telegramId = "tg-profile-5"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            age = 150, // Invalid age
            userId = voter.id,
            userRole = Roles.VOTER
        )

        val exception = assertThrows<IllegalArgumentException> {
            voterUseCase.updateProfile(updateDto)
        }

        assertEquals(Errors.ErrorCodes.AGE_IS_INVALID.name, exception.message)
    }

    @Test
    fun `updateProfile with invalid job length throws IllegalArgumentException`(): Unit = runBlocking {
        val telegramId = "tg-profile-6"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            job = "a".repeat(101), // Exceeds 100 characters
            userId = voter.id,
            userRole = Roles.VOTER
        )

        val exception = assertThrows<IllegalArgumentException> {
            voterUseCase.updateProfile(updateDto)
        }

        assertEquals(Errors.ErrorCodes.JOB_IS_INVALID.name, exception.message)
    }

    @Test
    fun `updateProfile with invalid field of study length throws IllegalArgumentException`(): Unit = runBlocking {
        val telegramId = "tg-profile-7"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            fieldOfStudy = "a".repeat(101), // Exceeds 100 characters
            userId = voter.id,
            userRole = Roles.VOTER
        )

        val exception = assertThrows<IllegalArgumentException> {
            voterUseCase.updateProfile(updateDto)
        }

        assertEquals(Errors.ErrorCodes.FIELD_OF_STUDY_IS_INVALID.name, exception.message)
    }

    @Test
    fun `updateProfile with invalid city length throws IllegalArgumentException`(): Unit = runBlocking {
        val telegramId = "tg-profile-8"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val updateDto = UpdateProfileDto(
            city = "a".repeat(101), // Exceeds 100 characters
            userId = voter.id,
            userRole = Roles.VOTER
        )

        val exception = assertThrows<IllegalArgumentException> {
            voterUseCase.updateProfile(updateDto)
        }

        assertEquals(Errors.ErrorCodes.CITY_IS_INVALID.name, exception.message)
    }

    @Test
    fun `updateProfile with all education levels works correctly`(): Unit = runBlocking {
        val telegramId = "tg-profile-9"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val educationLevels = listOf(
            EducationLevels.STUDENT,
            EducationLevels.BACHELOR,
            EducationLevels.MASTER,
            EducationLevels.DOCTORATE,
            EducationLevels.OTHER
        )

        educationLevels.forEach { educationLevel ->
            val updateDto = UpdateProfileDto(
                educationLevel = educationLevel,
                userId = voter.id,
                userRole = Roles.VOTER
            )

            val updatedVoter = voterUseCase.updateProfile(updateDto)
            assertEquals(educationLevel, updatedVoter.educationLevel)
        }
    }

    @Test
    fun `updateProfile with all marital statuses works correctly`(): Unit = runBlocking {
        val telegramId = "tg-profile-10"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val maritalStatuses = listOf(
            MaritalStatuses.MARRIED,
            MaritalStatuses.SINGLE
        )

        maritalStatuses.forEach { maritalStatus ->
            val updateDto = UpdateProfileDto(
                maritalStatus = maritalStatus,
                userId = voter.id,
                userRole = Roles.VOTER
            )

            val updatedVoter = voterUseCase.updateProfile(updateDto)
            assertEquals(maritalStatus, updatedVoter.maritalStatus)
        }
    }

    @Test
    fun `updateProfile with all genders works correctly`(): Unit = runBlocking {
        val telegramId = "tg-profile-11"
        val voter = voterUseCase.loginWithTelegram(telegramId)
        assertNotNull(voter.id)

        val genders = listOf(
            Genders.MALE,
            Genders.FEMALE
        )

        genders.forEach { gender ->
            val updateDto = UpdateProfileDto(
                gender = gender,
                userId = voter.id,
                userRole = Roles.VOTER
            )

            val updatedVoter = voterUseCase.updateProfile(updateDto)
            assertEquals(gender, updatedVoter.gender)
        }
    }

}
