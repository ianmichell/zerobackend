package com.zerobackend.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.gmongo.GMongo
import com.mongodb.CommandResult
import com.mongodb.WriteResult
import com.zerobackend.adapter.JsonToDBObjectAdapter
import com.zerobackend.model.Cursor
import com.zerobackend.ops.DBInsertOperation
import com.zerobackend.ops.DBQueryOperation
import com.zerobackend.ops.DBUpdateOperation

@Service
class DatabaseService {
	
	@Autowired
	protected JsonToDBObjectAdapter adapter
	
	@Autowired
	protected GMongo gmongo
	
	CommandResult stats(String database, String collection = null) {
		if (!collection) {
			return gmongo.getDB(database).stats
		}
		return gmongo.getDB(database)[collection].stats
	}
	
	void ensureIndex(String database, String collection, Map<String, Integer> values, Map<String, ?> options) {
		gmongo.getDB(database).getCollection(collection).ensureIndex(adapter.toDBObject(values), adapter.toDBObject(options))
	}
	
	Set<String> collectionNames(String database) {
		return gmongo.getDB(database).collectionNames
	}
	
	Cursor find(Closure c) {
		DBQueryOperation query = new DBQueryOperation(adapter, gmongo)
		c.delegate = query
		c()
		query.getCursor()
	}
	
	Map findOne(Closure c) {
		DBQueryOperation query = new DBQueryOperation(adapter, gmongo)
		c.delegate = query
		c()
		query.getSingleResult()
	}

	WriteResult update(Closure c) {
		DBUpdateOperation update = new DBUpdateOperation(adapter, gmongo)
		c.delegate = update
		c()
		update.update()
	}
	
	Map findAndModify(Closure c) {
		DBUpdateOperation update = new DBUpdateOperation(adapter, gmongo)
		c.delegate = update
		c()
		update.findAndModify()
	}
	
	WriteResult insert(Closure c) {
		DBInsertOperation insert = new DBInsertOperation(adapter, gmongo)
		c.delegate = insert
		c()
		insert.insert()
	}
	
}