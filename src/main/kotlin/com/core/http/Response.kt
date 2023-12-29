package com.core.http

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import jakarta.inject.Singleton

class Response {
    companion object {
        fun <T> ok(data: T): HttpResponse<T> {
            return HttpResponse.ok(
                data
            )
        }

        fun created(data: Any? = null): HttpResponse<*> {
            return HttpResponse.created(
                data
            )
        }

        fun badRequest(message: String, fields: List<FieldError>? = null): HttpResponse<*> {
            return HttpResponse.badRequest(
                ErrorResponse(
                    HttpStatus.BAD_REQUEST.name,
                    message,
                    fields
                )
            )
        }

        fun notFound(message: String): HttpResponse<*> {
            return HttpResponse.notFound(
                ErrorResponse(
                    HttpStatus.NOT_FOUND.name,
                    message
                )
            )
        }

        fun internalServerError(message: String): HttpResponse<*> {
            return HttpResponse.serverError(
                ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.name,
                    message
                )
            )
        }
    }
}