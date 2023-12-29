package com.modules.user

import arrow.core.Either
import com.core.http.Response
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import java.sql.SQLIntegrityConstraintViolationException

@Singleton
class UserService(private val userRepository: UserRepository) {
    fun create(command: CreateUserCommand): Either<Exception, User> {
        val user = User(command.email, command.cpf, command.name, command.birthDate)
        return try {
            Either.Right(userRepository.save(user))
        } catch (e: DataAccessException) {
            if (e.cause is SQLIntegrityConstraintViolationException) {
                return Either.Left(BadDataException("User with specified email or CPF already exists"))
            }
            return Either.Left(Exception("Internal Server Error"))
        }
    }

    fun findByNameLike(name: String, pageable: Pageable): Page<User> {
        return userRepository.findByNameLike(name, pageable)
    }

    fun findById(id: Long): Either<Exception, User> {
        val user = userRepository.findById(id)
        return if (user.isPresent) {
            Either.Right(user.get())
        } else {
            Either.Left(BadDataException("User with specified id does not exist"))
        }
    }

    fun update(id: Long, command: UpdateUserCommand): Either<Exception, User> {
        val user = findById(id)
        return user.fold(
            { Either.Left(it) },
            {
                val updatedUser =
                    it.copy(name = command.name ?: it.name, birthDate = command.birthDate ?: it.birthDate)
                Either.Right(userRepository.update(updatedUser))
            }
        )

    }
}