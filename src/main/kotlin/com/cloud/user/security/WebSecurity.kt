package com.cloud.user.security

import com.cloud.user.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurity(
    private val userService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val env: Environment
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.authorizeRequests().antMatchers("/actuator/**").permitAll()
        http.authorizeRequests().antMatchers("/**").permitAll().and().addFilter(getAuthenticationFilter())
        http.headers().frameOptions().disable()
    }

    private fun getAuthenticationFilter(): AuthenticationFilter {
        val authenticationFilter = AuthenticationFilter(userService, env)
        authenticationFilter.setAuthenticationManager(authenticationManager())
        return authenticationFilter
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder)
    }
}