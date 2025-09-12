package org.voting.user.unitTests

import org.junit.jupiter.api.Test
import org.voting.user.adaptor.api.mapper.UpdateVoterProfileMapper
import org.voting.user.domain.user.EducationLevels
import org.voting.user.domain.user.Genders
import org.voting.user.domain.user.MaritalStatuses
import org.voting.user.domain.user.Roles
import user.User
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UpdateVoterProfileMapperTest {

    private val mapper = UpdateVoterProfileMapper.mapper

    @Test
    fun `protoToDto maps all fields correctly`() {
        val userId = "user-123"
        val userRole = "VOTER"

        val request = User.UpdateVoterProfileRequest.newBuilder()
            .setCity("New York")
            .setGender(User.Genders.MALE)
            .setAge(25)
            .setJob("Software Engineer")
            .setEducationLevel(User.EducationLevels.BACHELOR)
            .setFieldOfStudy("Computer Science")
            .setMaritalStatus(User.MaritalStatuses.SINGLE)
            .build()

        val result = mapper.protoToDto(request, userId, userRole)

        assertNotNull(result)
        assertEquals("New York", result.city)
        assertEquals(Genders.MALE, result.gender)
        assertEquals(25, result.age)
        assertEquals("Software Engineer", result.job)
        assertEquals(EducationLevels.BACHELOR, result.educationLevel)
        assertEquals("Computer Science", result.fieldOfStudy)
        assertEquals(MaritalStatuses.SINGLE, result.maritalStatus)
        assertEquals(userId, result.userId)
        assertEquals(Roles.VOTER, result.userRole)
    }

    @Test
    fun `protoToDto maps partial fields correctly`() {
        val userId = "user-456"
        val userRole = "VOTER"

        val request = User.UpdateVoterProfileRequest.newBuilder()
            .setCity("London")
            .setAge(30)
            .build()

        val result = mapper.protoToDto(request, userId, userRole)

        assertNotNull(result)
        assertEquals("London", result.city)
        assertEquals(30, result.age)
        assertEquals(userId, result.userId)
        assertEquals(Roles.VOTER, result.userRole)
    }

    @Test
    fun `protoToDto maps empty request correctly`() {
        val userId = "user-789"
        val userRole = "VOTER"

        val request = User.UpdateVoterProfileRequest.newBuilder().build()

        val result = mapper.protoToDto(request, userId, userRole)

        assertNotNull(result)
        assertEquals(userId, result.userId)
        assertEquals(Roles.VOTER, result.userRole)
        assertEquals(null, result.city)
        assertEquals(null, result.age)
        assertEquals(null, result.job)
        assertEquals(null, result.fieldOfStudy)
    }

    @Test
    fun `protoToDto with FEMALE gender maps correctly`() {
        val userId = "user-female"
        val userRole = "VOTER"

        val request = User.UpdateVoterProfileRequest.newBuilder()
            .setGender(User.Genders.FEMALE)
            .build()

        val result = mapper.protoToDto(request, userId, userRole)

        assertEquals(Genders.FEMALE, result.gender)
    }

    @Test
    fun `protoToDto with all education levels maps correctly`() {
        val userId = "user-edu"
        val userRole = "VOTER"

        val educationLevels = listOf(
            User.EducationLevels.STUDENT to EducationLevels.STUDENT,
            User.EducationLevels.BACHELOR to EducationLevels.BACHELOR,
            User.EducationLevels.MASTER to EducationLevels.MASTER,
            User.EducationLevels.DOCTORATE to EducationLevels.DOCTORATE,
            User.EducationLevels.OTHER to EducationLevels.OTHER
        )

        educationLevels.forEach { (protoLevel, domainLevel) ->
            val request = User.UpdateVoterProfileRequest.newBuilder()
                .setEducationLevel(protoLevel)
                .build()

            val result = mapper.protoToDto(request, userId, userRole)

            assertEquals(domainLevel, result.educationLevel)
        }
    }

    @Test
    fun `protoToDto with all marital statuses maps correctly`() {
        val userId = "user-marital"
        val userRole = "VOTER"

        val maritalStatuses = listOf(
            User.MaritalStatuses.MARRIED to MaritalStatuses.MARRIED,
            User.MaritalStatuses.SINGLE to MaritalStatuses.SINGLE
        )

        maritalStatuses.forEach { (protoStatus, domainStatus) ->
            val request = User.UpdateVoterProfileRequest.newBuilder()
                .setMaritalStatus(protoStatus)
                .build()

            val result = mapper.protoToDto(request, userId, userRole)

            assertEquals(domainStatus, result.maritalStatus)
        }
    }
}
