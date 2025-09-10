package org.voting.poll.domain.ports.inbound

import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.dto.AddQuestionDTO
import org.voting.poll.domain.poll.dto.CreatePollDTO
import org.voting.poll.domain.poll.dto.UpdatePollDTO

interface PollUseCase {
    fun createPoll(createPollDto: CreatePollDTO): PollModel
    fun updatePoll(dto: UpdatePollDTO)
    fun addQuestion(dto: AddQuestionDTO)
}
