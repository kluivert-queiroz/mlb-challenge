package com.modules.user

import com.util.CPF
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension.getMock
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.sql.SQLIntegrityConstraintViolationException
import java.util.Optional

@MicronautTest
class UserServiceTest(private val userService: UserService, private val userRepository: UserRepository) : StringSpec({
    var id = 0;
    fun getCreateUserCommand() = CreateUserCommand(
        "john.doe${id++}@test.com",
        CPF.generate(),
        "John Doe",
        "2000-01-01"
    )

    "should create user" {
        val mockUserRepository = getMock(userRepository)
        every { mockUserRepository.save(any()) } answers { firstArg<User>() }
        val command = getCreateUserCommand()
        val user = userService.create(command)
        user.isRight() shouldBe true
    }
    "should handle error when creating user with existing email" {
        val mockUserRepository = getMock(userRepository)
        val exception = mockk<DataAccessException>()
        every { exception.cause } returns SQLIntegrityConstraintViolationException()
        every { mockUserRepository.save(any()) } throws exception
        val command = getCreateUserCommand()
        val user = userService.create(command)
        user.isLeft() shouldBe true
        user.swap().getOrNull() shouldBe BadDataException("User with specified email or CPF already exists")
    }
    "should find user by id" {
        val mockUserRepository = getMock(userRepository)
        val dummyUser = Optional.of(mockk<User>())
        every { mockUserRepository.findById(any()) } answers { dummyUser }
        val user = userService.findById(1)
        user.isRight() shouldBe true
    }
    "should handle missing user when finding user by id" {
        val mockUserRepository = getMock(userRepository)
        val dummyUser = Optional.empty<User>()
        every { mockUserRepository.findById(any()) } answers { dummyUser }
        val user = userService.findById(1)
        user.isLeft() shouldBe true
        user.swap().getOrNull() shouldBe BadDataException("User with specified id does not exist")
    }
    "should update user" {
        val mockUserRepository = getMock(userRepository)
        val dummyUser = User(1, "john.doe@test.com", CPF.generate(), "John Doe", "2000-01-01", null, null)
        every { mockUserRepository.findById(any()) } answers { Optional.of(dummyUser) }
        every { mockUserRepository.update(any()) } answers { firstArg<User>() }
        val command = UpdateUserCommand("John Doe", "2000-01-01")
        val user = userService.update(1, command)
        user.isRight() shouldBe true
    }
}) {
    @MockBean(UserRepository::class)
    fun userRepository(): UserRepository {
        return mockk()
    }
}