package com.core.http

import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.validation.exceptions.ConstraintExceptionHandler
import jakarta.inject.Singleton
import jakarta.validation.ConstraintViolationException

@Singleton
@Replaces(ConstraintExceptionHandler::class)
class CoreConstraintExceptionHandler : ExceptionHandler<ConstraintViolationException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>, exception: ConstraintViolationException): HttpResponse<*> {
        val errors = exception.constraintViolations.map {
            FieldError(it.propertyPath.drop(1).joinToString("."), it.message)
        }
        return Response.badRequest(
            "Invalid request", errors
        )
    }
}