package com.modules.user

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.Id
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@JdbcRepository(dialect = Dialect.MYSQL)
interface UserRepository : PageableRepository<User, Long> {
    fun findByNameLike(name: String, page: Pageable): Page<User>
    fun update(
        @NonNull @NotNull @Id id: Long,
        @NonNull @NotBlank name: String,
        @NonNull @NotNull birthDate: String
    ): Int
}