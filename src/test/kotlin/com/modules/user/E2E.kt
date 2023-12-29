package com.modules.user

import com.util.CPF
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import jakarta.inject.Inject
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime


@MicronautTest(transactional = false)
class E2E(@Inject @Client("/") private val client: HttpClient, private val userRepository: UserRepository) :
    StringSpec({
        var dummyId: Long = 0;
        fun addRandomUser(name: String = "John Doe"): User {
            dummyId++;
            return userRepository.save(User("test${dummyId}@test.com", CPF.generate(), name, "2000-01-01"))
        }
        beforeTest {
            com.core.Date.setNow(LocalDate.of(2020, 1, 1))
        }
        beforeEach {
            userRepository.deleteAll()
        }
        "should list paginated users" {
            val user = addRandomUser()
            addRandomUser()
            userRepository.count().shouldBe(2)
            val response = client.toBlocking().retrieve(
                HttpRequest.GET<Page<User>>("/users?page=0&pageSize=1"),
                Argument.of(Page::class.java as Class<Page<User>>, User::class.java)
            )
            response.totalSize.shouldBe(2)
            response.content.size.shouldBe(1)
            with(response.content[0]) {
                id.shouldBe(user.id)
                email.shouldBe(user.email)
                cpf.shouldBe(user.cpf)
            }
        }
        "should filter user by surname Doe" {
            val names = listOf("John Doe", "Mary Doe")
            names.forEach { addRandomUser(it) }
            addRandomUser("John Smith")
            userRepository.count().shouldBe(3)
            val response = client.toBlocking().retrieve(
                HttpRequest.GET<Page<User>>("/users?searchByName=%25Doe%25"),
                Argument.of(Page::class.java as Class<Page<User>>, User::class.java)
            )
            response.totalSize.shouldBe(names.size)
            response.content.size.shouldBe(names.size)
            names.forEachIndexed { index, name ->
                response.content[index].name.shouldBe(name)
            }

        }
        "should find user by id" {
            val user = addRandomUser()
            val response = client.toBlocking().retrieve(HttpRequest.GET<User>("/users/${user.id}"), User::class.java)
            response.id.shouldBe(user.id)
            response.email.shouldBe(user.email)
            response.cpf.shouldBe(user.cpf)
        }
        "should return not found when user is not found" {
            addRandomUser()
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().retrieve(HttpRequest.GET<User>("/users/439498"), User::class.java)
            }
            e.status.shouldBe(HttpStatus.NOT_FOUND)
        }
        "should save user when payload is valid" {
            val payload = CreateUserCommand(
                email = "test@test.com",
                cpf = CPF.generate(),
                name = "John Doe",
                birthDate = "2000-01-01"
            )
            val response = client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            response.status.shouldBe(HttpStatus.CREATED)
            response.body().shouldNotBeNull()
            with(response.body()) {
                id.shouldNotBeNull()
                email.shouldBe(payload.email)
                cpf.shouldBe(payload.cpf)
                birthDate.shouldBe(payload.birthDate)
            }
        }
        "should reject user less than 18 years old" {
            val payload = CreateUserCommand(
                email = "test@test.com",
                cpf = CPF.generate(),
                name = "John Doe",
                birthDate = "2020-01-01"
            )
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            }
            e.status.shouldBe(HttpStatus.BAD_REQUEST)
        }
        "should reject user with invalid email" {
            val payload = CreateUserCommand(
                email = "test",
                cpf = CPF.generate(),
                name = "John Doe",
                birthDate = "2000-01-01"
            )
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            }
            e.status.shouldBe(HttpStatus.BAD_REQUEST)
        }
        "should reject user with invalid cpf" {
            val payload = CreateUserCommand(
                email = "test@test.com",
                cpf = "12345678920",
                name = "John Doe",
                birthDate = "2000-01-01"
            )
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            }
            e.status.shouldBe(HttpStatus.BAD_REQUEST)
        }
        "should return 400 when duplicated email" {
            val user = addRandomUser()
            val payload = CreateUserCommand(
                email = user.email,
                cpf = CPF.generate(),
                name = "John Doe",
                birthDate = "2000-01-01"
            )
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            }
            e.status.shouldBe(HttpStatus.BAD_REQUEST)
        }
        "should return 400 when duplicated cpf" {
            val user = addRandomUser()
            val payload = CreateUserCommand(
                email = "test2@test.com",
                cpf = user.cpf,
                name = "John Doe",
                birthDate = "2000-01-01"
            )
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            }
            e.status.shouldBe(HttpStatus.BAD_REQUEST)
        }
        "should update user name" {
            val user = addRandomUser()
            val payload = UpdateUserCommand(name = "John Doe Dum")
            val response = client.toBlocking().exchange(
                HttpRequest.PUT("/users/${user.id}", payload), User::class.java
            )
            response.status.shouldBe(HttpStatus.OK)
            response.body().shouldNotBeNull()
            with(response.body()) {
                id.shouldNotBeNull()
                name.shouldBe(payload.name)
                updatedDate.shouldNotBe(user.updatedDate)
            }
        }
        "should update user birth date" {
            val user = addRandomUser()
            val payload = UpdateUserCommand(birthDate = "2000-01-01")
            val response = client.toBlocking().exchange(
                HttpRequest.PUT("/users/${user.id}", payload), User::class.java
            )
            response.status.shouldBe(HttpStatus.OK)
            response.body().shouldNotBeNull()
            with(response.body()) {
                id.shouldNotBeNull()
                name.shouldBe(user.name)
                birthDate.shouldBe(payload.birthDate)
                updatedDate.shouldNotBe(user.updatedDate)
            }
        }
        "should handle user not found when updating" {
            val payload = UpdateUserCommand(name = "John Doe Dum")
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(
                    HttpRequest.PUT("/users/123", payload), User::class.java
                )
            }
            e.status.shouldBe(HttpStatus.NOT_FOUND)
        }
    })