package com.cloud.user.vo

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RequestUser(
    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email no be less than two characters")
    @Email
    val email: String,

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name no be less than two characters")
    val name: String,

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters")
    val pwd: String
)