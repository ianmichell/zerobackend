package com.zerobackend.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class DatabaseWebsocketController {
	
	@MessageMapping("/find")
	public void find() {
		
	}
	
	@MessageMapping("/findOne")
	public void findOne() {
		
	}
	
	@MessageMapping("/update")
	public void update() {
		
	}
	
	@MessageMapping("/findAndModify")
	public void findAndModify() {
		
	}
	
	@MessageMapping("/remove")
	public void remove() {
		
	}
}
