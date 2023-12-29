package com.modules.user

import com.core.http.ErrorResponse
import com.core.http.Response
import com.fasterxml.jackson.core.type.TypeReference
import io.micronaut.core.type.Argument
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
import io.micronaut.openapi.annotation.OpenAPIGroup
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.Nullable
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.sql.SQLIntegrityConstraintViolationException
import kotlin.jvm.optionals.getOrNull

@Controller("/users")
@Tag(name = "Users")
open class UserController(private val userRepository: UserRepository) {

    @Operation(
        summary = "List users",
        description = "List paginated users. Optionally you can provide page, page size and search by name. See query parameters.",
        parameters = [
            Parameter(
                name = "page",
                `in` = ParameterIn.QUERY,
                description = "Page number. Starts at 0.",
                example = "0",
                schema = Schema(
                    defaultValue = "0"
                )
            ),
            Parameter(
                name = "pageSize",
                `in` = ParameterIn.QUERY,
                description = "Page size. Defaults to 50.",
                example = "50",
                schema = Schema(
                    defaultValue = "50"
                )
            ),
            Parameter(
                name = "searchByName",
                `in` = ParameterIn.QUERY,
                description = "Search by name. Defaults to %%. It support the SQL LIKE pattern. You can try '%20Doe%20' to fetch any name containing 'Doe'.",
                example = "%20Doe%20",
                schema = Schema(
                    defaultValue = "%%"
                )
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200", description = "Users fetched successfully", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = Page::class
                        )
                    )
                ]
            ),
        ]
    )
    @Get("/")
    open fun listUsers(
        @Nullable @PositiveOrZero @QueryValue page: Int? = 0,
        @Nullable @Positive @QueryValue pageSize: Int? = 50,
        @Nullable @QueryValue searchByName: String = "%%"
    ): HttpResponse<Page<User>> {
        return Response.ok(userRepository.findByNameLike(searchByName, Pageable.from(page!!, pageSize!!)))
    }

    @Operation(
        summary = "Find user by id",
        description = "Find user by id",
        parameters = [
            Parameter(
                name = "userId",
                `in` = ParameterIn.PATH,
                description = "User id",
                example = "1",
                schema = Schema(
                    implementation = Long::class
                )
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200", description = "User fetched successfully", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = User::class
                        )
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404", description = "User not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorResponse::class
                        )
                    )
                ]
            )
        ]
    )
    @Get("/{userId}")
    open fun findById(@Positive @PathVariable userId: Long): HttpResponse<*> {
        val user = userRepository.findById(userId)
        if (user.isEmpty) return Response.notFound("User not found")
        return Response.ok(user.get())
    }

    @Operation(
        summary = "Create user",
        description = "Create user",
        requestBody = RequestBody(
            description = "User creation payload",
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = CreateUserCommand::class
                    )
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "201", description = "User created successfully", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = User::class
                        )
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400", description = "Bad request",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorResponse::class
                        )
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal server error",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorResponse::class
                        )
                    )
                ]
            )
        ]
    )
    @Post("/")
    open fun create(@Body @Valid createUserCommand: CreateUserCommand): HttpResponse<*> {
        return try {
            Response.created(
                userRepository.save(
                    User(
                        createUserCommand.email,
                        createUserCommand.cpf,
                        createUserCommand.name,
                        createUserCommand.birthDate
                    )
                )
            )
        } catch (e: DataAccessException) {
            if (e.cause is SQLIntegrityConstraintViolationException) {
                return Response.badRequest("User with specified email or CPF already exists")
            }
            return Response.internalServerError("Internal Server Error")
        }
    }

    @Operation(
        summary = "Update user",
        description = "Update user",
        parameters = [
            Parameter(
                name = "userId",
                `in` = ParameterIn.PATH,
                description = "User id",
                example = "1",
                required = true,
                schema = Schema(
                    implementation = Long::class
                )
            )
        ],
        requestBody = RequestBody(
            description = "User update payload",
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(
                        implementation = UpdateUserCommand::class
                    )
                )
            ]
        ),
        responses = [
            ApiResponse(
                responseCode = "200", description = "User updated successfully", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = User::class
                        )
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400", description = "Bad request",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorResponse::class
                        )
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404", description = "User not found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorResponse::class
                        )
                    )
                ]
            )
        ]
    )
    @Put("/{userId}")
    open fun update(
        @PathVariable userId: Long, @Body @Valid updateUserCommand: UpdateUserCommand
    ): HttpResponse<*> {
        val user = userRepository.findById(userId).getOrNull() ?: return Response.notFound("User not found")
        val name = updateUserCommand.name ?: user.name
        val birthDate = updateUserCommand.birthDate ?: user.birthDate
        if(userRepository.update(userId, name, birthDate) > 0 ) {
            val fetchUser = userRepository.findById(userId).get()
            return Response.ok(fetchUser)
        }
        return Response.internalServerError("An error occurred while updating user")
    }
}