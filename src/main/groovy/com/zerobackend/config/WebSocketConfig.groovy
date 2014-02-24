package com.zerobackend.config

import java.util.List;

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker()
class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/events")
		config.setApplicationDestinationPrefixes("/app")
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// Queues for inbound requests
		registry.addEndpoint("/find").withSockJS()
		registry.addEndpoint("/findOne").withSockJS()
		registry.addEndpoint("/findAndModify").withSockJS()
		registry.addEndpoint("/update").withSockJS()
		registry.addEndpoint("/insert").withSockJS()
		registry.addEndpoint("/remove").withSockJS()
		
		// Authentication queue -> used for login, logout
		registry.addEndpoint("/auth").withSockJS()
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		return true
	}

}
