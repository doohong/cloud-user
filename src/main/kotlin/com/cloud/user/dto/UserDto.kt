package com.cloud.user.dto

import java.time.LocalDate
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserDto(
    var email: String = "",
    var name: String = "",
    var pwd: String = "",
    var userId: String = "",
    var createdAt: LocalDate = LocalDate.now(),

    var encryptedPwd: String = "",
)