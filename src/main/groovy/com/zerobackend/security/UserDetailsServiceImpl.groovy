package com.zerobackend.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import com.zerobackend.adapter.UserToMapAdapter
import com.zerobackend.services.DatabaseService

@Service("userService")
class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	DatabaseService databaseService
	
	@Autowired
	UserToMapAdapter userAdapter
	
	@Autowired
	PasswordEncoder passwordEncoder
	
	@Value('${zerobackend.db}')
	String db
	
	@Value('${zerobackend.collection.users}')
	String userCollection
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		def user = databaseService.findOne {
			database db
			collection userCollection
			query(["username":username])
		}
		userAdapter.toUser user
	}

	public void createUser(UserImpl user, String password) {
		user.password = passwordEncoder.encode password
		databaseService.insert {
			database db
			collection userCollection
			object(userAdapter.toMap(user))
		}
	}
}
