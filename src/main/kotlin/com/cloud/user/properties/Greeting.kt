package com.cloud.user.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("greeting")
class Greeting {
    var message: String = ""
}