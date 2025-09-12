package org.voting.user.integrationTests

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.voting.user.adaptor.persistance.repository.MongoVoterRepository
import org.voting.user.domain.ports.inbound.VoterUseCase
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
    fun `updateVoterInfo updates job, educationLevel, and fieldOfStudy`(): Unit = runBlocking {
        val telegramId = "tg-int-3"
        mongoVoterRepository.save(org.voting.user.adaptor.persistance.entity.Voter(telegramId = telegramId))

        val job = "Software Engineer"
        val educationLevel = "Bachelor"
        val fieldOfStudy = "Computer Science"

        val updated = voterUseCase.updateVoterInfo(
            telegramId = telegramId,
            job = job,
            educationLevel = educationLevel,
            fieldOfStudy = fieldOfStudy
        )

        assertEquals(job, updated.job)
        assertEquals(educationLevel, updated.educationLevel)
        assertEquals(fieldOfStudy, updated.fieldOfStudy)
    }

    @Test
    fun `updateVoterInfo fails for invalid job length`(): Unit = runBlocking {
        val telegramId = "tg-int-4"
        mongoVoterRepository.save(org.voting.user.adaptor.persistance.entity.Voter(telegramId = telegramId))

        val longJob = "a".repeat(101) // Exceeds 100 chars

        assertFailsWith<IllegalArgumentException> {
            voterUseCase.updateVoterInfo(
                telegramId = telegramId,
                job = longJob,
                educationLevel = "Bachelor",
                fieldOfStudy = "Computer Science"
            )
        }
    }

}
