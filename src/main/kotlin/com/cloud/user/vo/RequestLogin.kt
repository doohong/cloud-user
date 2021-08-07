package com.cloud.user.vo

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class RequestLogin{
    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email no be less than two characters")
    @Email
    lateinit var email: String

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters")
    lateinit var pwd: String
}