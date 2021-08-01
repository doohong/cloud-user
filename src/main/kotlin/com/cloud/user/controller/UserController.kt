package com.cloud.user.controller

import com.cloud.user.dto.UserDto
import com.cloud.user.jpa.UserEntity
import com.cloud.user.properties.Greeting
import com.cloud.user.service.UserService
import com.cloud.user.vo.RequestUser
import com.cloud.user.vo.ResponseUser
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class UserController(
    private val greeting: Greeting,
    private val userService: UserService,
) {
    @GetMapping("/health_check")
    fun status(): String {
        return "It's Working in User Service"
    }

    @GetMapping("/welcome")
    fun welcome(): String {
        return greeting.message
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