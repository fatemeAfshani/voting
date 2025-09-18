package org.voting.poll.unitTests

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.voting.poll.adaptor.exception.ForbiddenException
import org.voting.poll.adaptor.exception.NotFoundException
import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.PollOption
import org.voting.poll.domain.poll.PollQuestion
import org.voting.poll.domain.poll.enums.QuestionType
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.ports.outbound.persistance.PollRepository
import org.voting.poll.domain.ports.outbound.persistance.VoteRepository
import org.voting.poll.domain.ports.outbound.services.UserServiceInterface
import org.voting.poll.domain.vote.AnswerModel
import org.voting.poll.domain.vote.VoteModel
import org.voting.poll.domain.vote.VoteService
import org.voting.poll.domain.vote.dto.StartVoteDTO
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class VoteServiceTest {

    @Mock
    private lateinit var userService: UserServiceInterface

    @Mock
    private lateinit var voteRepository: VoteRepository

    @Mock
    private lateinit var pollRepository: PollRepository

    private lateinit var voteService: VoteService

    @BeforeEach
    fun setup() {
        voteService = VoteService(userService, voteRepository, pollRepository)
    }

    @Test
    fun `startVoting throws Forbidden when userId null or role not VOTER`() {
        assertThrows<ForbiddenException> {
            voteService.startVoting(StartVoteDTO(pollId = "p1", userId = null, role = Roles.VOTER))
        }
        assertThrows<ForbiddenException> {
            voteService.startVoting(StartVoteDTO(pollId = "p1", userId = "u1", role = Roles.CREATOR))
        }
    }

    @Test
    fun `startVoting throws NotFound when poll not found`() {
        whenever(pollRepository.findById("p-na"))
            .thenReturn(null)

        assertThrows<NotFoundException> {
            voteService.startVoting(StartVoteDTO(pollId = "p-na", userId = "u1", role = Roles.VOTER))
        }
    }

    @Test
    fun `startVoting throws Forbidden when preferences do not match`() {
        val poll = PollModel(
            id = "p1",
            creatorId = "c1",
            questions = listOf(PollQuestion(questionText = "Q1", questionType = QuestionType.EXPLAIN)),
            preferences = mapOf("city" to "Shiraz")
        )
        whenever(pollRepository.findById("p1")).thenReturn(poll)
        whenever(userService.getUserPreferences("u1")).thenReturn(mapOf("city" to "Tehran"))

        assertThrows<ForbiddenException> {
            voteService.startVoting(StartVoteDTO(pollId = "p1", userId = "u1", role = Roles.VOTER))
        }
    }

    @Test
    fun `startVoting creates vote if not exists and returns first question with no answer`() {
        val q1 = PollQuestion(
            questionText = "What?",
            questionType = QuestionType.MULTIPLE_CHOICE,
            options = listOf(PollOption(optionText = "A"), PollOption(optionText = "B")),
            shouldAnswer = true
        )
        val poll = PollModel(id = "p1", creatorId = "c1", questions = listOf(q1))
        whenever(pollRepository.findById("p1")).thenReturn(poll)
        whenever(userService.getUserPreferences("u1")).thenReturn(emptyMap())
        whenever(voteRepository.findByPollIdAndUserId("p1", "u1")).thenReturn(null)
        whenever(voteRepository.save(VoteModel(pollId = "p1", userId = "u1", answers = emptyList())))
            .thenReturn(VoteModel(id = "v1", pollId = "p1", userId = "u1", answers = emptyList()))

        val resp = voteService.startVoting(StartVoteDTO(pollId = "p1", userId = "u1", role = Roles.VOTER))

        assertEquals("p1", resp.pollId)
        assertEquals(q1.questionId, resp.firstQuestion.questionId)
        assertNull(resp.currentAnswer)
        verify(voteRepository).save(VoteModel(pollId = "p1", userId = "u1", answers = emptyList()))
    }

    @Test
    fun `startVoting uses existing vote and returns current answer when present`() {
        val q1 = PollQuestion(questionText = "Explain yourself", questionType = QuestionType.EXPLAIN)
        val poll = PollModel(id = "p2", creatorId = "c1", questions = listOf(q1))
        whenever(pollRepository.findById("p2")).thenReturn(poll)
        whenever(userService.getUserPreferences("u2")).thenReturn(mapOf("city" to "Tehran"))
        val existingVote = VoteModel(
            id = "v-existing",
            pollId = "p2",
            userId = "u2",
            answers = listOf(AnswerModel(questionId = q1.questionId, response = "My answer"))
        )
        whenever(voteRepository.findByPollIdAndUserId("p2", "u2")).thenReturn(existingVote)

        val resp = voteService.startVoting(StartVoteDTO(pollId = "p2", userId = "u2", role = Roles.VOTER))

        assertEquals("p2", resp.pollId)
        assertEquals(q1.questionId, resp.firstQuestion.questionId)
        assertEquals("My answer", resp.currentAnswer)
        verify(voteRepository).findByPollIdAndUserId("p2", "u2")
    }

    @Test
    fun `startVoting throws NotFound when poll has no questions`() {
        val poll = PollModel(id = "p3", creatorId = "c1", questions = emptyList())
        whenever(pollRepository.findById("p3")).thenReturn(poll)
        whenever(userService.getUserPreferences("u3")).thenReturn(emptyMap())

        assertThrows<NotFoundException> {
            voteService.startVoting(StartVoteDTO(pollId = "p3", userId = "u3", role = Roles.VOTER))
        }
    }
}
