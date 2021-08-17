package com.cloud.user.service

import com.cloud.user.client.OrderServiceClient
import com.cloud.user.dto.UserDto
import com.cloud.user.entity.UserEntity
import com.cloud.user.entity.UserRepository
import com.cloud.user.vo.ResponseOrder
import feign.FeignException
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val env: Environment,
    private val orderServiceClient: OrderServiceClient,
//    private val restTemplate: RestTemplate
) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(this::class.java)
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

    fun getUserDetailsByEmail(email: String): UserDto {
        val userEntity = userRepository.findByEmail(email)

        val mapper = ModelMapper()
        mapper.configuration.matchingStrategy = MatchingStrategies.STRICT

        return mapper.map(userEntity, UserDto::class.java)
    }

    fun getUserByUserID(userId: String): UserDto {
        val userEntity = userRepository.findByUserId(userId) ?: throw UsernameNotFoundException("user not found")

        val userDto = ModelMapper().map(userEntity, UserDto::class.java)
//        val orderUrl = String.format(env.getProperty("order_service_url")!!, userId)
//        val orderResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null, object : ParameterizedTypeReference<List<ResponseOrder>>(){})
//        val orders = orderResponse.body ?: listOf()
        try {
            val orders = orderServiceClient.getOrders(userId)
            userDto.orders = orders
        } catch (e: FeignException) {
            logger.error(e.message)
        }


        return userDto
    }
}