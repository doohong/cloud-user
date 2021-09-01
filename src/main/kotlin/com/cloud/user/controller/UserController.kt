package com.cloud.user.controller

import com.cloud.user.dto.UserDto
import com.cloud.user.properties.Greeting
import com.cloud.user.service.UserService
import com.cloud.user.vo.RequestUser
import com.cloud.user.vo.ResponseUser
import io.micrometer.core.annotation.Timed
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class UserController(
    private val env: Environment,
    private val greeting: Greeting,
    private val userService: UserService,
) {
    @GetMapping("/health_check")
    @Timed(value="users.status", longTask = true)
    fun status(): String {

        return "It's Working in User Service" +
            ", port =  ${env.getProperty("local.server.port")}" +
            ", token secret =  ${env.getProperty("token.secret")}" +
            ", token expiration time =  ${env.getProperty("token.expiration_time")}"
    }

    @GetMapping("/welcome")
    @Timed(value="users.welcome", longTask = true)
    fun welcome(): String {
        return greeting.message
    }

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable("userId") userId: String): ResponseEntity<ResponseUser> {
        val userDto = userService.getUserByUserID(userId)

        val responseUser = ModelMapper().map(userDto, ResponseUser::class.java)
        return ResponseEntity.status(HttpStatus.OK).body(responseUser)
    }


    @PostMapping("/users")
    fun createUser(@RequestBody user: RequestUser): ResponseEntity<Any>{
        val mapper = ModelMapper()
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT
        val userDto = mapper.map(user, UserDto::class.java)
        val responseUserDto = userService.createUser(userDto)
        val responseUser = mapper.map(responseUserDto, ResponseUser::class.java)

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser)
    }
}