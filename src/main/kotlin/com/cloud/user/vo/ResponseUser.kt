package com.cloud.user.vo

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ResponseUser(
    var email: String = "",
    var name: String = "",
    var userId: String = ""
)