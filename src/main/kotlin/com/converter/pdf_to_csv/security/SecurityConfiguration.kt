package com.converter.pdf_to_csv.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.inMemoryAuthentication()
            .withUser("ankur").password(this.passwordEncoder().encode("test")).roles("USER").and()
            .withUser("jana").password(this.passwordEncoder().encode("test")).roles("USER")
    }

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .httpBasic()
            .and()
            .csrf().disable()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }
}