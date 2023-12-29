package com.modules.user

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.Instant


@Serdeable
@MappedEntity
data class User(
    @Id @GeneratedValue(GeneratedValue.Type.AUTO) var id: Long?,
    @NotNull @Schema(example = "john.doe@test.com") val email: String,
    @NotNull @Schema(example = "47277852001") val cpf: String,
    @NotNull @Schema(example = "John Doe") val name: String,
    @NotNull val birthDate: String,
    @DateCreated var createdDate: Instant?,
    @DateUpdated var updatedDate: Instant?
) {
    constructor(email: String, cpf: String, name: String, birthDate: String) : this(
        null,
        email,
        cpf,
        name,
        birthDate,
        null,
        null
    )
}