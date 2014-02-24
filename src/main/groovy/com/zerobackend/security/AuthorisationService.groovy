package com.zerobackend.security

import java.io.Serializable;
import java.security.Principal

import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service

@Service("authorisationService")
class AuthorisationService {

	boolean canAccessDatabase(String database, Principal principal) {
		println "${database} ${principal}"
		true
	}
	
	boolean canAccessDatabase(String database, String principal) {
		println "${database} ${principal}"
		true
	}

}
