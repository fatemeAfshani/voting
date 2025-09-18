package org.voting.poll.domain.poll.dto

data class PollReportResponseDTO(
    val excelFile: ByteArray,
    val fileName: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PollReportResponseDTO

        if (!excelFile.contentEquals(other.excelFile)) return false
        if (fileName != other.fileName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = excelFile.contentHashCode()
        result = 31 * result + fileName.hashCode()
        return result
    }
}
