package com.cloud.user.service

import com.cloud.user.dto.UserDto
import com.cloud.user.entity.UserEntity
import com.cloud.user.entity.UserRepository
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : UserDetailsService {
    fun createUser(userDto: UserDto): UserDto {
        userDto.userId = UUID.randomUUID().toString()

        val mapper = ModelMapper()
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT

        val userEntity = mapper.map(userDto, UserEntity::class.java)
        userEntity.encryptedPwd = passwordEncoder.encode(userDto.pwd)

        userRepository.save(userEntity)

        return mapper.map(userEntity, UserDto::class.java)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByEmail(username) ?: throw UsernameNotFoundException(username)

        return User(userEntity.email, userEntity.encryptedPwd, true, true, true, true, listOf())
    }
}