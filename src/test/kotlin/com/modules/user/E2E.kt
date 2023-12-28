package com.modules.user

import com.util.CPF
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import jakarta.inject.Inject


@MicronautTest(transactional = false)
class E2E(@Inject @Client("/") private val client: HttpClient, private val userRepository: UserRepository) :
    StringSpec({
        var dummyId: Long = 0;
        fun addRandomUser(): User {
            dummyId++;
            return userRepository.save(User("test${dummyId}@test.com", CPF.generate(), "John Doe"))
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
            val payload = CreateUserCommand(email = "test@test.com", cpf = CPF.generate(), name = "John Doe")
            val response = client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            response.status.shouldBe(HttpStatus.CREATED)
            response.body().shouldNotBeNull()
            with(response.body()) {
                id.shouldNotBeNull()
                email.shouldBe(payload.email)
                cpf.shouldBe(payload.cpf)
            }
        }
        "should return 422 when duplicated email" {
            val user = addRandomUser()
            val payload = CreateUserCommand(email = user.email, cpf = CPF.generate(), name = "John Doe")
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            }
            e.status.shouldBe(HttpStatus.BAD_REQUEST)
        }
        "should return 422 when duplicated cpf" {
            val user = addRandomUser()
            val payload = CreateUserCommand(email = "test2@test.com", cpf = user.cpf, name = "John Doe")
            val e = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST("/users", payload), User::class.java)
            }
            e.status.shouldBe(HttpStatus.BAD_REQUEST)
        }
        "should update user" {
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
            }
        }
    })