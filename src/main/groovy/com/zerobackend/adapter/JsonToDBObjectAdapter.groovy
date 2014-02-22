package com.zerobackend.adapter

import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.DBObject

@Component
class JsonToDBObjectAdapter {

	@Autowired
	ObjectMapper mapper
	
	DBObject parseText(String json) {
		Map query = mapper.convertValue(json, LinkedHashMap.class)
		return toDBObject(query)
	}
	
	DBObject toDBObject(Map query) {
		// TODO: We can perform query validaton here...
		
		// We have a map based mongodb query. We need to ensure some of the correct types are used.
		// For instance {$date: "<UTC>"}
		DBObject dbObject = new BasicDBObject()
		
		// Convert the map to the db object
		processMap query, dbObject
		
		return dbObject
	}
	
	Map<String, ?> toMap(DBObject o) {
		if (!o) {
			return null
		}
		o.collectEntries { key, value ->
			// We only need to replace object ids...
			def newValue = value
			if (value instanceof ObjectId) {
				newValue = ['$oid': value.toString()]
			}
			[(key): newValue]
		}
	}
	
	private void processMap(Map from, DBObject to) {
		from.each { key, value ->
			if (value instanceof Map) {
				Map mapValue = (Map)value
				if (mapValue.containsKey('$oid')) {
					to[key] = new ObjectId(mapValue['$oid'])
				} else if (mapValue.containsKey('$date')) {
					Date d = mapValue['$date'] instanceof Number ? new Date(mapValue['$date']) : DateTime.parse(mapValue['$date']).toDate()
					to[key] = d
				} else if (value instanceof Collection) {
					to[key] = processCollection((Collection)value)				
				} else {
					DBObject child = new BasicDBObject()
					processMap mapValue, child
					to[key] = child
				}
			} else if (value instanceof Collection) {
				to[key] = processCollection((Collection)value)
			} else {
				to[key] = value
			}
		}
	}
	
	private List processCollection(Collection c) {
		return c.collect {
			if (it instanceof Map) {
				DBObject child = new BasicDBObject()
				processMap((Map)it, child)
				return child
			} else if (it instanceof Collection) { // should never happen
				return processCollection((Collection)it)
			} else {
				return it
			}
		}
	}
}
