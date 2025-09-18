package org.voting.poll.domain.poll

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.voting.poll.adaptor.exception.Errors
import org.voting.poll.adaptor.exception.ForbiddenException
import org.voting.poll.adaptor.exception.NotFoundException
import org.voting.poll.adaptor.exception.UnknownException
import org.voting.poll.domain.poll.dto.ActivePollsDTO
import org.voting.poll.domain.poll.dto.AddQuestionDTO
import org.voting.poll.domain.poll.dto.CreatePollDTO
import org.voting.poll.domain.poll.dto.UpdatePollDTO
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.poll.enums.QuestionType
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.ports.inbound.PollUseCase
import org.voting.poll.domain.ports.outbound.persistance.PollRepository
import org.voting.poll.domain.ports.outbound.services.UserServiceInterface


@Service
class PollService(
    private val pollRepository: PollRepository,
    private val userService: UserServiceInterface
) : PollUseCase {

    override fun createPoll(createPollDto: CreatePollDTO): PollModel {
        val (title, description, userId) = createPollDto
        val poll = PollModel(title = title, description = description, creatorId = userId)
        // todo: check if there is unpaid poll, do not create new one
        val createdPoll = pollRepository.insert(poll) ?: throw UnknownException(Errors.ErrorCodes.UNKNOWN_ERROR.name)
        return createdPoll
    }

    override fun updatePoll(dto: UpdatePollDTO) {
        val poll = pollRepository.findById(dto.pollId) ?: throw NotFoundException()
        if (poll.creatorId != dto.userId) throw ForbiddenException()
        // todo: check adding preferences
        poll.updateWithNewData(dto.title, dto.description, dto.maxVoters, dto.preferences)
        pollRepository.save(poll)
    }

    override fun addQuestion(dto: AddQuestionDTO) {
        val poll = pollRepository.findById(dto.pollId) ?: throw NotFoundException()
        if (poll.creatorId != dto.userId) throw ForbiddenException()

        val options = when (dto.questionType) {
            QuestionType.EXPLAIN -> emptyList()
            else -> dto.options.map { optionText -> PollOption(optionText = optionText) }
        }

        val question = PollQuestion(
            questionText = dto.questionText,
            questionType = dto.questionType,
            options = options,
            shouldAnswer = dto.shouldAnswer
        )

        val existingQuestions = poll.questions.toMutableList()
        existingQuestions.add(question)
        poll.questions = existingQuestions

        pollRepository.save(poll)
    }

    override fun getActivePolls(userId: String?, role: Roles?): List<ActivePollsDTO> {
        if (userId == null || role != Roles.VOTER) {
            throw ForbiddenException()
        }
        val activePolls = pollRepository.findByStatus(PollStatus.ACTIVE)

        val voterPreferences = userService.getUserPreferences(userId)

        return activePolls.filter { poll ->
            val prefs = poll?.preferences
            if (prefs.isNullOrEmpty()) {
                true
            } else {
                // if at least one preference key/value matches, include
                prefs.any { (k, v) -> voterPreferences[k] == v }
            }
        }.map { ActivePollsDTO(it?.title, it?.description, it?.preferences) }
    }
}
