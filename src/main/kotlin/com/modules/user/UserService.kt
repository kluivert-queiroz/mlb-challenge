package com.modules.user

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.right
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import java.sql.SQLIntegrityConstraintViolationException

@Singleton
class UserService(private val userRepository: UserRepository) {
    fun create(command: CreateUserCommand): Either<UserProblem, User> {
        val user = User(command.email, command.cpf, command.name, command.birthDate)
        return catch({ userRepository.save(user).right() }) {
            if (it.cause is SQLIntegrityConstraintViolationException) {
                return@catch UserProblem.DuplicatedEmailOrCPF.left()
            }
            throw it
        }

    }

    fun findByNameLike(name: String, pageable: Pageable): Page<User> {
        return userRepository.findByNameLike(name, pageable)
    }

    fun findById(id: Long): Either<UserProblem, User> {
        return either {
            userRepository.findById(id).orElseThrow { raise(UserProblem.NotFound(id)) }
        }
    }

    fun update(id: Long, command: UpdateUserCommand): Either<UserProblem, User> {
        val user = findById(id)
        return user.fold({ Either.Left(it) }, {
            val updatedUser = it.copy(name = command.name ?: it.name, birthDate = command.birthDate ?: it.birthDate)
            Either.Right(userRepository.update(updatedUser))
        })

    }
}