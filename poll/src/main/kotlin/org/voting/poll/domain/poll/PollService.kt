package org.voting.poll.domain.poll

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.voting.poll.adaptor.exception.Errors
import org.voting.poll.adaptor.exception.ForbiddenException
import org.voting.poll.adaptor.exception.NotFoundException
import org.voting.poll.adaptor.exception.UnknownException
import org.voting.poll.domain.poll.dto.ActivePollsDTO
import org.voting.poll.domain.poll.dto.AddQuestionDTO
import org.voting.poll.domain.poll.dto.CreatePollDTO
import org.voting.poll.domain.poll.dto.PollReportDTO
import org.voting.poll.domain.poll.dto.PollReportResponseDTO
import org.voting.poll.domain.poll.dto.UpdatePollDTO
import org.voting.poll.domain.poll.util.ExcelReportGenerator
import org.voting.poll.domain.poll.enums.PollStatus
import org.voting.poll.domain.poll.enums.QuestionType
import org.voting.poll.domain.poll.enums.Roles
import org.voting.poll.domain.ports.inbound.PollUseCase
import org.voting.poll.domain.ports.outbound.persistance.PollRepository
import org.voting.poll.domain.ports.outbound.persistance.VoteRepository
import org.voting.poll.domain.ports.outbound.services.UserServiceInterface
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@Service
class PollService(
    private val pollRepository: PollRepository,
    private val userService: UserServiceInterface,
    private val voteRepository: VoteRepository,
    @Value("\${poll.reports.directory:reports}")
    private val reportsDirectory: String,
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
                prefs.any { (k, v) -> voterPreferences[k] == v }
            }
        }.map { ActivePollsDTO(it?.title, it?.description, it?.preferences, it?.id) }
    }

    override fun getReport(dto: PollReportDTO): PollReportResponseDTO {
        if (dto.userId == null || dto.role != Roles.CREATOR) {
            throw ForbiddenException()
        }

        val poll = pollRepository.findById(dto.pollId) ?: throw NotFoundException()
        if (poll.creatorId != dto.userId) {
            throw ForbiddenException()
        }

        val votes = voteRepository.findByPollId(dto.pollId)

        val excelGenerator = ExcelReportGenerator()
        val excelBytes = excelGenerator.generatePollReport(poll, votes)

        val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
        val fileName = "poll_report_${poll.title?.replace(" ", "_") ?: poll.id}_$timestamp.xlsx"

        // Save the Excel file to disk
        val reportsDir = File(reportsDirectory)
        if (!reportsDir.exists()) {
            reportsDir.mkdirs()
        }
        
        val filePath = Paths.get(reportsDirectory, fileName)
        Files.write(filePath, excelBytes)
        
        println("Excel report saved to: ${filePath.toAbsolutePath()}")

        return PollReportResponseDTO(excelBytes, fileName)
    }
}
