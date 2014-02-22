

package com.zerobackend.config.startup

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import com.zerobackend.security.UserDetailsServiceImpl
import com.zerobackend.security.UserImpl
import com.zerobackend.services.DatabaseService

@Component
class ApplicationInitEvent implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	DatabaseService databaseService
	
	@Autowired
	UserDetailsServiceImpl userDetailsService
	
	@Value('${zerobackend.db}')
	String db
	
	@Value('${zerobackend.collection.users}')
	String userCollection
	
	@Value('${zerobackend.user}')
	String username
	
	@Value('${zerobackend.password}')
	String password
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		setupDefaultUser()
	}
	
	private void setupDefaultUser() {
		UserDetails user = userDetailsService.loadUserByUsername(username) 
		if (!user) {
			user = new UserImpl(username: username, enabled:true, accountNonExpired:true, credentialsNonExpired:true, accountNonLocked:true, authorities:[new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")])
			databaseService.ensureIndex(db, userCollection, ["username": 1], ["unique":true])
			userDetailsService.createUser(user, password)
		}
	}
}
