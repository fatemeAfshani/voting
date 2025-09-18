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
import org.voting.poll.domain.poll.PollService
import org.voting.poll.domain.poll.dto.PollReportDTO
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.ports.outbound.persistance.PollRepository
import org.voting.poll.domain.ports.outbound.persistance.VoteRepository
import org.voting.poll.domain.ports.outbound.services.UserServiceInterface
import org.voting.poll.domain.vote.AnswerModel
import org.voting.poll.domain.vote.VoteModel
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class PollServiceTest {

    @Mock
    private lateinit var pollRepository: PollRepository

    @Mock
    private lateinit var userService: UserServiceInterface

    @Mock
    private lateinit var voteRepository: VoteRepository

    private lateinit var pollService: PollService

    @BeforeEach
    fun setup() {
        pollService = PollService(pollRepository, userService, voteRepository, "test-reports")
    }

    @Test
    fun `getActivePolls throws Forbidden when role not VOTER`() {
        assertThrows<ForbiddenException> {
            pollService.getActivePolls("user-1", Roles.CREATOR)
        }
        assertThrows<ForbiddenException> {
            pollService.getActivePolls(null, Roles.VOTER)
        }
    }

    @Test
    fun `polls with no preferences are included`() {
        val userId = "user-1"
        val polls = listOf(
            PollModel(creatorId = "c1", title = "A", description = "D", preferences = null),
            PollModel(creatorId = "c2", title = "B", description = "E", preferences = emptyMap())
        )
        whenever(pollRepository.findByStatus(PollStatus.ACTIVE)).thenReturn(polls)
        whenever(userService.getUserPreferences(userId)).thenReturn(mapOf("city" to "Tehran"))

        val result = pollService.getActivePolls(userId, Roles.VOTER)

        assertEquals(2, result.size)
        verify(pollRepository).findByStatus(PollStatus.ACTIVE)
        verify(userService).getUserPreferences(userId)
    }

    @Test
    fun `polls with preferences included when at least one matches`() {
        val userId = "user-2"
        val polls = listOf(
            PollModel(creatorId = "c1", title = "A", description = "D", preferences = mapOf("city" to "Tehran")),
            PollModel(
                creatorId = "c2",
                title = "B",
                description = "E",
                preferences = mapOf("gender" to "FEMALE", "age" to "30")
            )
        )
        whenever(pollRepository.findByStatus(PollStatus.ACTIVE)).thenReturn(polls)
        whenever(userService.getUserPreferences(userId)).thenReturn(mapOf("city" to "Tehran", "gender" to "MALE"))

        val result = pollService.getActivePolls(userId, Roles.VOTER)

        assertEquals(1, result.size)
        assertEquals("A", result[0].title)
    }

    @Test
    fun `polls with preferences excluded when none match`() {
        val userId = "user-3"
        val polls = listOf(
            PollModel(creatorId = "c1", title = "A", description = "D", preferences = mapOf("city" to "Shiraz")),
            PollModel(creatorId = "c2", title = "B", description = "E", preferences = mapOf("gender" to "FEMALE"))
        )
        whenever(pollRepository.findByStatus(PollStatus.ACTIVE)).thenReturn(polls)
        whenever(userService.getUserPreferences(userId)).thenReturn(mapOf("city" to "Tehran", "gender" to "MALE"))

        val result = pollService.getActivePolls(userId, Roles.VOTER)

        assertEquals(0, result.size)
    }

    @Test
    fun `getReport throws Forbidden when userId null or role not CREATOR`() {
        assertThrows<ForbiddenException> {
            pollService.getReport(PollReportDTO(pollId = "p1", userId = null, role = Roles.CREATOR))
        }
        assertThrows<ForbiddenException> {
            pollService.getReport(PollReportDTO(pollId = "p1", userId = "u1", role = Roles.VOTER))
        }
    }

    @Test
    fun `getReport throws NotFound when poll not found`() {
        whenever(pollRepository.findById("p-na")).thenReturn(null)

        assertThrows<NotFoundException> {
            pollService.getReport(PollReportDTO(pollId = "p-na", userId = "u1", role = Roles.CREATOR))
        }
    }

    @Test
    fun `getReport throws Forbidden when user is not poll creator`() {
        val poll = PollModel(id = "p1", creatorId = "creator1", title = "Test Poll")
        whenever(pollRepository.findById("p1")).thenReturn(poll)

        assertThrows<ForbiddenException> {
            pollService.getReport(PollReportDTO(pollId = "p1", userId = "different-user", role = Roles.CREATOR))
        }
    }

    @Test
    fun `getReport generates Excel file with poll data and votes`() {
        val poll = PollModel(
            id = "p1",
            creatorId = "creator1",
            title = "Test Poll",
            questions = listOf(
                org.voting.poll.domain.poll.PollQuestion(
                    questionId = "q1",
                    questionText = "What is your favorite color?",
                    questionType = org.voting.poll.domain.poll.enums.QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        org.voting.poll.domain.poll.PollOption(optionId = "o1", optionText = "Red"),
                        org.voting.poll.domain.poll.PollOption(optionId = "o2", optionText = "Blue")
                    )
                )
            )
        )
        val votes = listOf(
            VoteModel(
                id = "v1",
                pollId = "p1",
                userId = "voter1",
                answers = mutableListOf(AnswerModel(questionId = "q1", response = "o1")),
                createdAt = java.util.Date()
            )
        )

        whenever(pollRepository.findById("p1")).thenReturn(poll)
        whenever(voteRepository.findByPollId("p1")).thenReturn(votes)

        val result = pollService.getReport(PollReportDTO(pollId = "p1", userId = "creator1", role = Roles.CREATOR))

        assertTrue(result.fileName.startsWith("poll_report_Test_Poll_"), "Filename should start with poll_report_Test_Poll_")
        assertTrue(result.fileName.endsWith(".xlsx"), "Filename should end with .xlsx")
        assert(result.excelFile.isNotEmpty())
        
        // Verify file was saved to disk
        val savedFile = File("test-reports", result.fileName)
        assertTrue(savedFile.exists(), "Excel file should be saved to disk")
        assertTrue(savedFile.length() > 0, "Saved file should not be empty")
        
        // Clean up test file
        savedFile.delete()
    }
}
