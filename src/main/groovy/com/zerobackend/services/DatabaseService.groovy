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

@Service("databaseService")
class DatabaseService {
	
	@Autowired
	protected JsonToDBObjectAdapter adapter
	
	@Autowired
	protected GMongo gmongo
	
	CommandResult stats(String database, String collection = null) {
		if (!collection) {
			return gmongo.getDB(database).getStats()
		}
		return gmongo.getDB(database)[collection].getStats()
	}
	
	void ensureIndex(String database, String collection, Map<String, Integer> values, Map<String, ?> options) {
		gmongo.getDB(database).getCollection(collection).ensureIndex(adapter.toDBObject(values), adapter.toDBObject(options))
	}
	
	public boolean databaseExists(String database) {
		return databaseNames().contains(database)
	}
	
	public boolean collectionExists(String database, String collection) {
		databaseNames().contains(database) && gmongo.getDB(database).collectionExists(collection)
	}
	
	List<String> databaseNames() {
		return gmongo.getDatabaseNames()
	}
	
	Set<String> collectionNames(String database) {
		return gmongo.getDB(database).getCollectionNames()
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