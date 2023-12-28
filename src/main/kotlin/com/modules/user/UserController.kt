package com.modules.user

import com.core.MessageSource
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import jakarta.annotation.Nullable
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.sql.SQLIntegrityConstraintViolationException

@Controller("/users")
open class UserController(private val userRepository: UserRepository, private val messageSource: MessageSource) {

    @Get("/")
    open fun listUsers(
        @Nullable @PositiveOrZero @QueryValue("page") page: Int? = 0,
        @Nullable @Positive @QueryValue("pageSize") pageSize: Int? = 50,
        @Nullable @QueryValue searchByName: String = "%%"
    ): HttpResponse<Page<User>> {
        return HttpResponse.ok(userRepository.findByNameLike(searchByName, Pageable.from(page!!, pageSize!!)))
    }

    @Get("/{userId}")
    open fun findById(@Positive @PathVariable userId: Long): HttpResponse<User> {
        val user = userRepository.findById(userId)
        if (user.isEmpty) return HttpResponse.notFound()
        return HttpResponse.ok(user.get())
    }

    @Post("/")
    open fun create(@Body @Valid createUserCommand: CreateUserCommand): HttpResponse<*> {
        return try {
            HttpResponse.created(
                userRepository.save(
                    User(createUserCommand.email, createUserCommand.cpf, createUserCommand.name)
                )
            )
        } catch (e: DataAccessException) {
            if (e.cause is SQLIntegrityConstraintViolationException) {
                return HttpResponse.badRequest(mapOf("errors" to listOf("Informed email or CPF already exists.")))
            }
            return HttpResponse.serverError("Internal Server Error")
        }
    }

    @Put("/{userId}")
    open fun update(
        @PathVariable userId: Long, @Body @Valid updateUserCommand: UpdateUserCommand
    ): HttpResponse<User> {
        userRepository.update(userId, updateUserCommand.name)
        return HttpResponse.ok(userRepository.findById(userId).get())
    }

    @Error(exception = ConstraintViolationException::class)
    fun onConstraintFail(request: HttpRequest<Any>, ex: ConstraintViolationException): Map<String, Any> {
        return mapOf("errors" to messageSource.violationsMessages(ex.constraintViolations))
    }
}