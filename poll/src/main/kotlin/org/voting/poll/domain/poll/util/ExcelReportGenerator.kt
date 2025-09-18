package org.voting.poll.domain.poll.util

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.voting.poll.domain.poll.PollModel
import org.voting.poll.domain.poll.PollQuestion
import org.voting.poll.domain.poll.enums.QuestionType
import org.voting.poll.domain.vote.VoteModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ExcelReportGenerator {

    fun generatePollReport(poll: PollModel, votes: List<VoteModel>): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Poll Report")

        val headerStyle = createHeaderStyle(workbook)
        val dateStyle = createDateStyle(workbook)

        val headers = createHeaders(poll.questions)
        val headerRow = sheet.createRow(0)
        headers.forEachIndexed { index, header ->
            val cell = headerRow.createCell(index)
            cell.setCellValue(header)
            cell.cellStyle = headerStyle as org.apache.poi.xssf.usermodel.XSSFCellStyle
        }

        votes.forEachIndexed { voteIndex, vote ->
            val row = sheet.createRow(voteIndex + 1)

            row.createCell(0).setCellValue("Vote ${voteIndex + 1}")
            row.createCell(1).setCellValue(formatDate(vote.createdAt))
            row.createCell(2).setCellValue(if (vote.isFinished) "Completed" else "In Progress")

            poll.questions.forEachIndexed { questionIndex, question ->
                val answer = vote.answers.find { it.questionId == question.questionId }
                val answerText = answer?.let { formatAnswer(it.response, question) } ?: "No Answer"
                row.createCell(questionIndex + 3).setCellValue(answerText)
            }
        }

        headers.forEachIndexed { index, _ ->
            sheet.autoSizeColumn(index)
        }

        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()

        return outputStream.toByteArray()
    }

    private fun createHeaders(questions: List<PollQuestion>): List<String> {
        val baseHeaders = listOf("Vote ID", "Date", "Status")
        val questionHeaders = questions.map { it.questionText }
        return baseHeaders + questionHeaders
    }

    private fun formatAnswer(answer: String, question: PollQuestion): String {
        return when (question.questionType) {
            QuestionType.EXPLAIN -> answer
            else -> {
                val option = question.options.find { it.optionId == answer }
                option?.optionText ?: answer
            }
        }
    }

    private fun formatDate(date: Date?): String {
        return date?.let {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(it)
        } ?: "Unknown"
    }

    private fun createHeaderStyle(workbook: Workbook): CellStyle {
        val style = workbook.createCellStyle()
        val font = workbook.createFont()
        font.bold = true
        style.setFont(font)
        style.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        style.fillPattern = FillPatternType.SOLID_FOREGROUND
        return style
    }

    private fun createDateStyle(workbook: Workbook): CellStyle {
        val style = workbook.createCellStyle()
        val format = workbook.createDataFormat()
        style.dataFormat = format.getFormat("yyyy-mm-dd hh:mm:ss")
        return style
    }
}
