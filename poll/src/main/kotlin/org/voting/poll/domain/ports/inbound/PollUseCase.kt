package org.voting.poll.domain.ports.inbound

import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.dto.ActivePollsDTO
import org.voting.poll.domain.poll.dto.AddQuestionDTO
import org.voting.poll.domain.poll.dto.CreatePollDTO
import org.voting.poll.domain.poll.dto.PollReportDTO
import org.voting.poll.domain.poll.dto.PollReportResponseDTO
import org.voting.poll.domain.poll.dto.UpdatePollDTO
import org.voting.poll.domain.poll.enums.Roles

interface PollUseCase {
    fun createPoll(createPollDto: CreatePollDTO): PollModel
    fun updatePoll(dto: UpdatePollDTO)
    fun addQuestion(dto: AddQuestionDTO)

    fun getActivePolls(userId: String?, role: Roles?): List<ActivePollsDTO>
    fun getReport(dto: PollReportDTO): PollReportResponseDTO
}
