package com.zerobackend.security

import groovy.transform.CompileStatic;
import groovy.transform.ToString;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails

@ToString(includes=["username, enabled, accountNonExpired, accountNonLocked, credentialsNonExpired, authorities"])
@CompileStatic
class UserImpl implements UserDetails {
	
	String id
	Set<SimpleGrantedAuthority> authorities = []
	String password
	String username
	boolean accountNonExpired
	boolean accountNonLocked
	boolean credentialsNonExpired
	boolean enabled
	
}
