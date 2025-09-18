package org.voting.poll.domain.ports.inbound

import org.voting.poll.domain.vote.dto.StartVoteDTO
import org.voting.poll.domain.vote.dto.StartVoteResponse
import org.voting.poll.domain.vote.dto.AnswerQuestionDTO
import org.voting.poll.domain.vote.dto.QuestionResponseDTO

interface VoteUseCase {

    fun startVoting(data: StartVoteDTO): StartVoteResponse

    fun answerQuestion(data: AnswerQuestionDTO): QuestionResponseDTO
}
