package org.voting.poll.domain.vote

import org.springframework.stereotype.Service
import org.voting.poll.adaptor.exception.ForbiddenException
import org.voting.poll.adaptor.exception.NotFoundException
import org.voting.poll.domain.poll.enums.QuestionType
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.poll.validateAnswerOrThrow
import org.voting.poll.domain.ports.inbound.VoteUseCase
import org.voting.poll.domain.ports.outbound.persistance.PollRepository
import org.voting.poll.domain.ports.outbound.persistance.VoteRepository
import org.voting.poll.domain.ports.outbound.services.UserServiceInterface
import org.voting.poll.domain.vote.dto.AnswerQuestionDTO
import org.voting.poll.domain.vote.dto.QuestionResponseDTO
import org.voting.poll.domain.vote.dto.StartVoteDTO
import org.voting.poll.domain.vote.dto.StartVoteResponse

@Service
class VoteService(
    private val userService: UserServiceInterface,
    private val voteRepository: VoteRepository,
    private val pollRepository: PollRepository,
) : VoteUseCase {
    override fun startVoting(data: StartVoteDTO): StartVoteResponse {
        if (data.userId == null || data.role != Roles.VOTER) {
            throw ForbiddenException()
        }

        val poll = pollRepository.findById(data.pollId) ?: throw NotFoundException()

        val voterPreferences = userService.getUserPreferences(data.userId)
        val pollPrefs = poll.preferences
        if (!pollPrefs.isNullOrEmpty()) {
            val matched = pollPrefs.any { (k, v) -> voterPreferences[k] == v }
            if (!matched) throw ForbiddenException()
        }

        val existingVote = voteRepository.findByPollIdAndUserId(data.pollId, data.userId)
        val vote = existingVote ?: voteRepository.save(
            VoteModel(
                pollId = data.pollId,
                userId = data.userId,
            )
        )

        val firstQuestion = poll.questions.firstOrNull() ?: throw NotFoundException()
        val answer = vote.answers.firstOrNull { it.questionId == firstQuestion.questionId }?.response

        return StartVoteResponse(firstQuestion = firstQuestion, currentAnswer = answer, pollId = poll.id)
    }

    override fun answerQuestion(data: AnswerQuestionDTO): QuestionResponseDTO {
        require(data.userId != null && data.role == Roles.VOTER) { throw ForbiddenException() }

        val poll = pollRepository.findById(data.pollId) ?: throw NotFoundException()
        val vote = voteRepository.findByPollIdAndUserId(data.pollId, data.userId) ?: throw NotFoundException()

        val questions = poll.questions

        val currentQuestion = poll.getQuestionById(data.questionId) ?: throw NotFoundException()
        if (currentQuestion.questionType != QuestionType.EXPLAIN) {
            currentQuestion.validateAnswerOrThrow(data.answer)
        }

        vote.upsertAnswer(data.questionId, data.answer)

        val currentIndex = poll.findQuestionIndexById(data.questionId) ?: throw NotFoundException()
        val nextQuestion = questions.getOrNull(currentIndex + 1)
        val prevQuestion = questions.getOrNull(currentIndex - 1)

        val nextAnswer = nextQuestion?.let { q -> vote.findAnswer(q.questionId)?.response }
        val prevAnswer = prevQuestion?.let { q -> vote.findAnswer(q.questionId)?.response }

        if (nextQuestion == null) {
            vote.isFinished = true
        }

        voteRepository.save(vote)

        return QuestionResponseDTO(
            nextQuestion = nextQuestion ?: questions.last(),
            nextQuestionAnswer = nextAnswer,
            previousQuestion = prevQuestion ?: questions.first(),
            previousQuestionAnswer = prevAnswer,
            pollId = data.pollId
        )
    }
}
