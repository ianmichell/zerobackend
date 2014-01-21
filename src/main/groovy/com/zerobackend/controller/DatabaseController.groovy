package com.zerobackend.controller

import groovy.json.JsonSlurper

import java.util.concurrent.Callable

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import com.gmongo.GMongo
import com.mongodb.DBCursor
import com.mongodb.DBObject

/**
 * This is just a basic query controller for now...
 * @author ian
 *
 */
@RestController
class DatabaseController {
	
	@Autowired
	GMongo gmongo	
	
	@RequestMapping(value="/{database}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<Set<String>> showCollections(@PathVariable("database") String database) {
		return new Callable<Set<String>>() {
			@Override
			public Set<String> call() {
				 gmongo.getDB(database).getCollectionNames()
			}
		}
	}
	
	@RequestMapping(value="/{database}/stats", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<DBObject> databaseInfo(@PathVariable("database") String database) {
		return new Callable<DBObject>() {
			@Override
			public DBObject call() {
				 gmongo.getDB(database).getStats()
			}
		}
	}
	
	@RequestMapping(value="/{database}/{collection}/stats", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<DBObject> collectionInfo(@PathVariable("database") String database, @PathVariable("collection") String collection) {
		return new Callable<DBObject>() {
			@Override
			public DBObject call() {
				 gmongo.getDB(database).getCollection(collection).getStats()
			}
		}
	}
	
	@RequestMapping(value="/{database}/{collection}/find", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<List<DBObject>> find(@PathVariable("database") String database, @PathVariable("collection") collectionName, 
		@RequestParam(value="query", defaultValue="{}") String query, 
		@RequestParam(value="fields", defaultValue="{}") String fields, @RequestParam(value="sort", defaultValue="{}") String sort,
		@RequestParam(value="offset", defaultValue="0") int offset, @RequestParam(value="limit", defaultValue="100") int limit) {
		
		return new Callable<Iterable<DBObject>>() {
			@Override
			public List<DBObject> call() {
				// Parse the query
				def q = new JsonSlurper().parseText(query)
				def f = new JsonSlurper().parseText(fields)
				def s = new JsonSlurper().parseText(sort)
				DBCursor cursor = gmongo.getDB(database).getCollection(collectionName).find(q, f).sort(s).skip(offset).limit(limit)
				return cursor.toList()
			}
		}
	}
		
		@RequestMapping(value="/{database}/{collection}/findOne", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		Callable<DBObject> findOne(@PathVariable("database") String database, @PathVariable("collection") collectionName,
			@RequestParam(value="query", defaultValue="{}") String query) {
			
			return new Callable<DBObject>() {
				@Override
				public DBObject call() {
					// Parse the query
					def q = new JsonSlurper().parseText(query)
					return gmongo.getDB(database).getCollection(collectionName).findOne(q)
				}
			}
		}
	
	@RequestMapping(value="/{database}/{collection}/{documentId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<DBObject> get(@PathVariable("database") String database, @PathVariable("collection") String collectionName, @PathVariable("documentId") String id) {
		return new Callable<DBObject>() {
			@Override
			public DBObject call() {
				 gmongo.getDB(database).getCollection(collectionName).findOne(["_id": id])
			}
		}
	}
}
