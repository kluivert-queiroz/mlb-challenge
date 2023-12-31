package com.modules.user

interface UserProblem {
    data class NotFound(val userId: Long) : UserProblem
    object DuplicatedEmailOrCPF: UserProblem
}