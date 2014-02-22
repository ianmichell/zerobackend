package com.zerobackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonParser.Feature
import com.fasterxml.jackson.databind.ObjectMapper

@ComponentScan()
@EnableAutoConfiguration
class Application {

//	@Bean
//	public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean() {
//		Jackson2ObjectMapperFactoryBean factory = new Jackson2ObjectMapperFactoryBean();
//		factory.setFeaturesToEnable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, JsonParser.Feature.ALLOW_SINGLE_QUOTES);
//		return factory;
//	}
	
	@Bean
	public ObjectMapper objectMapper() {
		println "Mapper"
		ObjectMapper mapper = new ObjectMapper()
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
		mapper
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
