package com.cloud.user.service

import com.cloud.user.dto.UserDto
import com.cloud.user.jpa.UserEntity
import com.cloud.user.jpa.UserRepository
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(userDto: UserDto): UserDto {
        userDto.userId = UUID.randomUUID().toString()

        val mapper = ModelMapper()
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT

        val userEntity = mapper.map(userDto, UserEntity::class.java)
        userEntity.encryptedPwd = "encrypted_password"

        userRepository.save(userEntity)

        return mapper.map(userEntity, UserDto::class.java)
    }
}