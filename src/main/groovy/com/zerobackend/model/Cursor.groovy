package com.zerobackend.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
class Cursor {

	Map query
	Map fields
	Map sort
	int offset
	int limit
	long totalElements
	List<Map> content
	
}
