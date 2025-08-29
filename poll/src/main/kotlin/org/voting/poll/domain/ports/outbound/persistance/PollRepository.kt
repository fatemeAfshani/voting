package org.voting.poll.domain.ports.outbound.persistance

import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.enums.PollStatus

interface PollRepository {
    fun save(poll: PollModel): PollModel?
    fun findById(id: String): PollModel?
    fun findByStatus(status: PollStatus): List<PollModel?>
}
