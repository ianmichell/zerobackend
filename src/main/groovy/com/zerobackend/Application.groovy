package com.zerobackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonParser.Feature
import com.fasterxml.jackson.databind.ObjectMapper


@Configuration
@ComponentScan()
@EnableAutoConfiguration()
class Application {
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper()
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
		mapper
	}
	
	@Bean()
	public PasswordEncoder configurePasswordEncoder() {
		new BCryptPasswordEncoder(16)
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter json2HttpMessageConverter(ObjectMapper mapper) {
		MappingJackson2HttpMessageConverter c = new MappingJackson2HttpMessageConverter()
		c.objectMapper = mapper
		return c
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args)
	}
}
