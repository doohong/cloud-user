package com.cloud.user.security

import com.cloud.user.service.UserService
import com.cloud.user.vo.RequestLogin
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
        private val userService: UserService,
        private val env: Environment
): UsernamePasswordAuthenticationFilter() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        val creds = ObjectMapper().readValue(request.inputStream, RequestLogin::class.java)
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(creds.email, creds.pwd, listOf()))
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
//        super.successfulAuthentication(request, response, chain, authResult)
        val userName = (authResult.principal as User).username
        val userDetails = userService.getUserDetailsByEmail(userName)
        val token = Jwts.builder()
                .setSubject(userDetails.userId)
                .setExpiration(Date(System.currentTimeMillis() + (env.getProperty("token.expiration_time")?.toLong() ?: 0)))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact()

        response.addHeader("token", token)
        response.addHeader("userId", userDetails.userId)
    }
}