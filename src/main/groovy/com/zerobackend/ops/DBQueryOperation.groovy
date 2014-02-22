package com.zerobackend.ops

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import com.gmongo.GMongo
import com.mongodb.BasicDBObject
import com.mongodb.DBCursor
import com.mongodb.DBObject
import com.zerobackend.adapter.JsonToDBObjectAdapter
import com.zerobackend.model.Cursor

class DBQueryOperation {
	
	public DBQueryOperation(JsonToDBObjectAdapter adapter, GMongo gmongo) {
		this.adapter = adapter
		this.gmongo = gmongo
	}
	
	private JsonToDBObjectAdapter adapter
	private GMongo gmongo
	
	private String _database
	private String _collection
	private DBObject _query = new BasicDBObject()
	private Map<String, ?> _originalQuery = [:]
	private DBObject _fields = new BasicDBObject()
	private DBObject _sort
	private int _skip
	private int _limit
	
	
	DBQueryOperation database(String database) {
		this._database = database
		this
	}
	
	DBQueryOperation collection(String collection) {
		this._collection = collection
		this
	}
	
	DBQueryOperation query(Map<String, ?> query) {
		this._originalQuery = query
		this._query = adapter.toDBObject(query)
		this
	}
	
	DBQueryOperation fields(Map<String, ?> fields) {
		this._fields = adapter.toDBObject(fields)
		this
	}
	
	DBQueryOperation sort(Map<String, ?> sort) {
		this._sort = adapter.toDBObject(sort)
		this
	}
	
	DBQueryOperation skip(int skip) {
		this._skip = skip
		this
	}
	
	DBQueryOperation limit(int limit) {
		this._limit = limit
		this
	}
	
	DBQueryOperation call() {
		this
	}
	
	private Cursor getCursor() {
		DBCursor cursor = gmongo.getDB(_database)[_collection].find(_query, _fields)
		if (_sort) {
			cursor = cursor.sort(_sort)
		}
		cursor = cursor.skip(_skip)
		if (_limit > 0) {
			cursor = cursor.limit(_limit)
		}
		Cursor c = new Cursor()
		c.content = cursor.toArray().collect {
			adapter.toMap it
		}
		c.fields = _fields
		c.query = _originalQuery
		c.sort = _sort
		c.offset = _skip
		c.limit = _limit
		c.totalElements = cursor.count()
		c
	}
	
	private Map getSingleResult() {
		adapter.toMap gmongo.getDB(_database)[_collection].findOne(_query)
	}
}
