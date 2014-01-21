package com.zerobackend.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class HelloController {
	
	@MessageMapping("/hello")
	@SendTo("/queue/greetings")
	public Map<String, String> greeting(Map<String, String> message) throws Exception {
		return ["message": "Hello " + message.name]
	}
}
