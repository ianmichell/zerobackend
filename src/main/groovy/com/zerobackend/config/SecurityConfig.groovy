package com.zerobackend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity(debug=true)
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true, proxyTargetClass=true)
class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value('${zerobackend.db}')
	String zerobackend

	@Autowired(required=true)
	public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService, PasswordEncoder encoder) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder)
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// Anything for zero backend other than the administrator must be blocked automatically...
				.antMatchers("/db", "/db/", "/db/test/**", "/db/*/oplog/**", "/db/${zerobackend}/**").hasRole("ADMINISTRATOR")
				.anyRequest().permitAll().and().formLogin()
	}
}
