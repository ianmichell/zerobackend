package com.zerobackend.controller

import groovy.json.JsonSlurper

import java.util.concurrent.Callable

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import com.gmongo.GMongo
import com.mongodb.CommandResult
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

	@RequestMapping(value="/{database}/{collection}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<DBObject> queryCollection(@PathVariable("database") String database, @PathVariable("collection") String collection, 
		@RequestParam(value="query", required=false) String query, @RequestParam(value="fields",required=false) String fields, @RequestParam(value="sort", required=false) String sort, 
		@RequestParam(value="offset", defaultValue="0") int offset, @RequestParam(value="max", defaultValue="100") int max) {
		return new Callable<List<DBObject>>() {
			@Override
			public List<DBObject> call() throws Exception {
				def db = gmongo.getDB(database)
				def dbCollection = db.getCollection(collection)
				
				if (StringUtils.isEmpty(query)) {
					CommandResult result = db.getStats()
					return [result]
				} else {
					def dbQuery = new JsonSlurper().parseText(query)
					def dbFields = StringUtils.hasText(fields) ? new JsonSlurper().parseText(fields) : null
					return dbCollection.find(dbQuery, dbFields, offset, max).collect { it }
				}
			}
		}
	}
}
