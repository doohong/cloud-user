package com.cloud.user.entity

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Long> {
    fun findByEmail(email: String) : UserEntity?
}