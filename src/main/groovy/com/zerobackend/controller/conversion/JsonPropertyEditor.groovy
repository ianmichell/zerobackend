package com.zerobackend.controller.conversion

import java.beans.PropertyEditorSupport

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.fasterxml.jackson.databind.ObjectMapper

@Component
class JsonPropertyEditor extends PropertyEditorSupport {

	@Autowired
	ObjectMapper mapper
	
	public void setAsText(String text) {
		setValue(mapper.readValue(text, Map.class))
	}

	@Override
	public String getAsText() {
		return mapper.writeValueAsString(getValue())
	}

	
}