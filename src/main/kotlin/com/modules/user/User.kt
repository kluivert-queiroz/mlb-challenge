package com.modules.user

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.GenerationType
import jakarta.validation.constraints.NotNull


@Serdeable
@MappedEntity
data class User(
    @Id @GeneratedValue(GeneratedValue.Type.AUTO) var id: Long?,
    @NotNull val email: String,
    @NotNull val cpf: String,
    @NotNull val name: String,
){
    constructor(email: String, cpf: String, name: String) : this(null, email, cpf, name)
}