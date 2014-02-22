package com.zerobackend.adapter

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

import com.zerobackend.security.UserImpl

@Component
class UserToMapAdapter {
	
	Map<String, ?> toMap(UserImpl user) {
		if (user == null) {
			return null
		}
		user.class.declaredFields.findAll { !it.synthetic }.collectEntries {
			if (it.name == "authorities") {
				return [(it.name): user[it.name].collect { it.authority }]
			}
			if (it.name == "id") {
				return [_id: user[it.name]]
			}
			[(it.name): user[it.name]]
		}
	}
	
	UserImpl toUser(Map<String, ?> map) {
		if (map == null) {
			return null
		}
		new UserImpl(map.collectEntries {
			if (it.key == "authorities") {
				return [(it.key): it.value.collect { r-> new SimpleGrantedAuthority(r) }]
			}
			if (it.key == "_id") {
				return ["id": it.value]
			}
			[(it.key): it.value]
		})
	}
	
}
