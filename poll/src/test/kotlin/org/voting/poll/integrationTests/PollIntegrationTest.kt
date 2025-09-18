package org.voting.poll.integrationTests

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.voting.poll.adaptor.api.mapper.AddQuestionMapper
import org.voting.poll.adaptor.api.mapper.CreatePollMapper
import org.voting.poll.adaptor.api.mapper.UpdatePollMapper
import org.voting.poll.adaptor.persistance.repository.MongoPollRepository
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.poll.enums.QuestionType
import org.voting.poll.domain.ports.inbound.PollUseCase
import org.voting.poll.shared.Fixtures
import poll.Poll
import poll.Poll.AddQuestionRequest
import poll.Poll.CreatePollRequest
import poll.Poll.UpdatePollRequest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.voting.poll.adaptor.persistance.entity.Poll as PollEntity

@Testcontainers
@SpringBootTest
@ExtendWith(MockitoExtension::class)
class PollIntegrationTest {

    @Autowired
    private lateinit var pollService: PollUseCase

    @Autowired
    private lateinit var mongoPollRepository: MongoPollRepository

    companion object {
        @Container
        val mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)

        init {
            mongoDBContainer.start()
            System.setProperty("spring.data.mongodb.uri", mongoDBContainer.replicaSetUrl)
        }
    }

    @BeforeEach
    fun setup() {
        mongoPollRepository.deleteAll()
    }

    @Test
    fun `createPoll should work via gRPC call`(): Unit = runBlocking {
        val request = CreatePollRequest.newBuilder()
            .setTitle("Test Poll Title")
            .setDescription("Test Poll Description")
            .build()

        val createdPoll = pollService.createPoll(
            CreatePollMapper.mapper.protoToDto(request, Fixtures.mockCreatorRole, Fixtures.mockCreatorId)
        )

        assertNotNull(createdPoll.id)
        val data = mongoPollRepository.findOneById(createdPoll.id!!)
        assertEquals(data?.creatorId, Fixtures.mockCreatorId)
        assertEquals(data?.status, PollStatus.ACTIVE)
    }

    @Test
    fun `update poll should work via gRPC call`(): Unit = runBlocking {
        val poll = PollEntity(creatorId = Fixtures.mockCreatorId)
        val createdPoll = mongoPollRepository.insert(poll)

        val request = UpdatePollRequest.newBuilder()
            .setTitle("Test Poll Title")
            .setDescription("Test Poll Description")
            .setPollId(createdPoll.id)
            .setMaxVoters(100)
            .build()

        pollService.updatePoll(
            UpdatePollMapper.mapper.protoToDto(request, Fixtures.mockCreatorRole, Fixtures.mockCreatorId)
        )

        val data = mongoPollRepository.findOneById(createdPoll.id!!)
        assertEquals(data?.creatorId, Fixtures.mockCreatorId)
        assertEquals(data?.title, "Test Poll Title")
        assertEquals(data?.description, "Test Poll Description")
        assertEquals(data?.maxVoters, 100)
        assertEquals(data?.status, PollStatus.ACTIVE)
    }

    @Test
    fun `addQuestion should work via gRPC call`(): Unit = runBlocking {
        val poll = PollEntity(creatorId = Fixtures.mockCreatorId)
        val createdPoll = mongoPollRepository.insert(poll)

        val request = AddQuestionRequest.newBuilder()
            .setPollId(createdPoll.id!!)
            .setQuestionText("What is your favorite programming language?")
            .setQuestionType(Poll.QuestionType.MULTIPLE_CHOICE)
            .addAllOptions(listOf("Kotlin", "Java", "Python", "JavaScript"))
            .setShouldAnswer(false)
            .build()

        // When
        pollService.addQuestion(
            AddQuestionMapper.mapper.protoToDto(request, Fixtures.mockCreatorRole, Fixtures.mockCreatorId)
        )

        // Then - Verify the poll was updated with the question
        val updatedPoll = mongoPollRepository.findOneById(createdPoll.id!!)
        assertNotNull(updatedPoll)
        assertEquals(1, updatedPoll.questions.size)
        assertEquals("What is your favorite programming language?", updatedPoll.questions.first().questionText)
        assertEquals(QuestionType.MULTIPLE_CHOICE, updatedPoll.questions.first().questionType)
        assertEquals(4, updatedPoll.questions.first().options.size)
        assertEquals(false, updatedPoll.questions.first().shouldAnswer)
    }

    @Test
    fun `addQuestion with EXPLAIN type should work`(): Unit = runBlocking {
        // Given
        val poll = PollEntity(creatorId = Fixtures.mockCreatorId)
        val createdPoll = mongoPollRepository.insert(poll)

        val request = AddQuestionRequest.newBuilder()
            .setPollId(createdPoll.id!!)
            .setQuestionText("Please explain your experience with microservices")
            .setQuestionType(Poll.QuestionType.EXPLAIN)
            .build()

        // When
        pollService.addQuestion(
            AddQuestionMapper.mapper.protoToDto(request, Fixtures.mockCreatorRole, Fixtures.mockCreatorId)
        )

        // Then
        val updatedPoll = mongoPollRepository.findOneById(createdPoll.id!!)
        assertNotNull(updatedPoll)
        assertEquals(1, updatedPoll.questions.size)
        assertEquals("Please explain your experience with microservices", updatedPoll.questions.first().questionText)
        assertEquals(QuestionType.EXPLAIN, updatedPoll.questions.first().questionType)
        assertEquals(0, updatedPoll.questions.first().options.size)
    }

    @Test
    fun `addQuestion with TWO_OPTION type should work`(): Unit = runBlocking {
        // Given
        val poll = PollEntity(creatorId = Fixtures.mockCreatorId)
        val createdPoll = mongoPollRepository.insert(poll)

        val request = AddQuestionRequest.newBuilder()
            .setPollId(createdPoll.id!!)
            .setQuestionText("Do you prefer REST or GraphQL?")
            .setQuestionType(Poll.QuestionType.TWO_OPTION)
            .addAllOptions(listOf("REST", "GraphQL"))
            .build()

        // When
        pollService.addQuestion(
            AddQuestionMapper.mapper.protoToDto(request, Fixtures.mockCreatorRole, Fixtures.mockCreatorId)
        )

        // Then
        val updatedPoll = mongoPollRepository.findOneById(createdPoll.id!!)
        assertNotNull(updatedPoll)
        assertEquals(1, updatedPoll.questions.size)
        assertEquals("Do you prefer REST or GraphQL?", updatedPoll.questions.first().questionText)
        assertEquals(QuestionType.TWO_OPTION, updatedPoll.questions.first().questionType)
        assertEquals(2, updatedPoll.questions.first().options.size)
        assertEquals("REST", updatedPoll.questions.first().options.first().optionText)
        assertEquals("GraphQL", updatedPoll.questions.first().options.last().optionText)
    }

    @Test
    fun `addQuestion with UNRECOGNIZED type should default to EXPLAIN`(): Unit = runBlocking {
        // Given
        val poll = PollEntity(creatorId = Fixtures.mockCreatorId)
        val createdPoll = mongoPollRepository.insert(poll)

        val request = AddQuestionRequest.newBuilder()
            .setPollId(createdPoll.id!!)
            .setQuestionText("Unknown question type test")
            .build()

        // When
        pollService.addQuestion(
            AddQuestionMapper.mapper.protoToDto(request, Fixtures.mockCreatorRole, Fixtures.mockCreatorId)
        )

        // Then
        val updatedPoll = mongoPollRepository.findOneById(createdPoll.id!!)
        assertNotNull(updatedPoll)
        assertEquals(1, updatedPoll.questions.size)
        assertEquals("Unknown question type test", updatedPoll.questions.first().questionText)
        assertEquals(QuestionType.EXPLAIN, updatedPoll.questions.first().questionType)
    }
}
