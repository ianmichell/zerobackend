package com.zerobackend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.authentication.UserCredentials
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

import com.gmongo.GMongo
import com.mongodb.Mongo
import com.mongodb.MongoOptions
import com.mongodb.ServerAddress

@Configuration
@EnableMongoRepositories(basePackages="com.zerobackend.repositories")
class MongoConfig {

	@Value('${dataSource.url:}') String url
	@Value('${dataSource.database:zerobackend}') String database
	@Value('${dataSource.username:}') String username
	@Value('${dataSource.password:}') String password
	
	@Bean
	GMongo gmongo() {
		return new GMongo(mongo())
	}
	
	@Bean
	Mongo mongo() {
		// Setup url list for mongodb replica set...
		List<ServerAddress> serverAddresses = new LinkedList<ServerAddress>();
		for (String address : url.split(";")) {
			String[] hostPort = address.split(":")
			String host = hostPort[0]
			int port = hostPort.length > 1 ? new Integer(hostPort[1]) : 27017
			ServerAddress a = new ServerAddress(host, port)
		}
		
		// FIXME: TODO
		MongoOptions options = new MongoOptions();
		
		Mongo mongo = null;
		
		if (serverAddresses.size() > 1) {
			mongo = new Mongo(serverAddresses)
		} else if (serverAddresses.size() == 1) {
			mongo = new Mongo(serverAddresses.get(0))
		} else {
			mongo = new Mongo()
		}
		return mongo
	}
	
	@Bean
	MongoDbFactory mongoDBFactory() {
		UserCredentials userCredentials = new UserCredentials(username, password);
		new SimpleMongoDbFactory(mongo(), database, userCredentials)
	}
	
	@Bean
	MongoTemplate mongoTemplate() {
		new MongoTemplate(mongoDBFactory())
	}
}
