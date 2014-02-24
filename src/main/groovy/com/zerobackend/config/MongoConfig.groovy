package com.zerobackend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import com.gmongo.GMongo
import com.mongodb.Mongo

@Configuration
class MongoConfig {

//	@Value('${dataSource.url:}') String url
//	@Value('${dataSource.database:zerobackend}') String database
//	@Value('${dataSource.username:}') String username
//	@Value('${dataSource.password:}') String password
	
	@Bean
	@Autowired
	GMongo gmongo(Mongo mongo) {
		return new GMongo(mongo)
	}
	
//	@Bean
//	Mongo mongo() {
//		// Setup url list for mongodb replica set...
//		List<ServerAddress> serverAddresses = new LinkedList<ServerAddress>();
//		for (String address : url.split(";")) {
//			String[] hostPort = address.split(":")
//			String host = hostPort[0]
//			int port = hostPort.length > 1 ? new Integer(hostPort[1]) : 27017
//			ServerAddress a = new ServerAddress(host, port)
//		}
//		
//		// FIXME: TODO
//		MongoOptions options = new MongoOptions();
//		
//		Mongo mongo = null;
//		
//		if (serverAddresses.size() > 1) {
//			mongo = new Mongo(serverAddresses)
//		} else if (serverAddresses.size() == 1) {
//			mongo = new Mongo(serverAddresses.get(0))
//		} else {
//			mongo = new Mongo()
//		}
//		return mongo
//	}
	
}
