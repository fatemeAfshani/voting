//package org.voting.usermanagement.unitTests
//
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito.`when`
//import org.mockito.junit.jupiter.MockitoExtension
//import org.voting.usermanagement.adaptor.persistance.dataMapperImp.PollRepositoryImp
//import org.voting.usermanagement.domain.creator.CreatorModel
//import org.voting.usermanagement.domain.creator.CreatorService
//import org.voting.usermanagement.domain.creator.dto.RegisterDto
//import kotlin.test.assertEquals
//
//@ExtendWith(MockitoExtension::class)
//class CreatorServiceTest {
//
//    @InjectMocks
//    private lateinit var service: CreatorService
//
//    @Mock
//    private lateinit var pollRepositoryImp: PollRepositoryImp
//
//    @Test
//    fun `should register creator`() {
//        val request = RegisterDto("1234567890", "password123", "testUser")
//        service.register(request)
//    }
//
//    @Test
//    fun `should login creator successfully`() {
//        val request = RegisterDto("1234567890", "password123")
//        val creator = CreatorModel(id = "1", phone = "1234567890", password = "password123", userName = "testUser")
//
//        `when`(pollRepositoryImp.findByPhoneNumber("1234567890")).thenReturn(creator)
//
//        val result = service.login(request)
//
//        assertEquals(creator, result)
//    }
//}
