package org.voting.poll.unitTests

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.voting.poll.adaptor.exception.ForbiddenException
import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.PollService
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.ports.outbound.persistance.PollRepository
import org.voting.poll.domain.ports.outbound.services.UserServiceInterface
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever

@ExtendWith(MockitoExtension::class)
class PollServiceTest {

    @Mock
    private lateinit var pollRepository: PollRepository

    @Mock
    private lateinit var userService: UserServiceInterface

    private lateinit var pollService: PollService

    @BeforeEach
    fun setup() {
        pollService = PollService(pollRepository, userService)
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
}
