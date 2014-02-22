package com.zerobackend.ops

import com.gmongo.GMongo
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.mongodb.WriteResult
import com.zerobackend.adapter.JsonToDBObjectAdapter

class DBUpdateOperation {
	
	private JsonToDBObjectAdapter adapter
	private GMongo gmongo
	
	private String _database
	private String _collection
	
	// Fields
	private DBObject _query = new BasicDBObject()
	private DBObject _update = new BasicDBObject()
	private DBObject _sort
	private DBObject _fields = new BasicDBObject()
	private boolean _upsert = false
	private boolean _multi = false
	private boolean _returnNew = true // default this to true always
	private boolean _remove = false
	
	
	public DBUpdateOperation(JsonToDBObjectAdapter adapter, GMongo mongo) {
		this.adapter = adapter;
		this.gmongo = mongo;
	}
	
	DBUpdateOperation database(String database) {
		_database = database
		this
	}
	
	DBUpdateOperation collection(String collection) {
		_collection = collection
		this
	}
	
	DBUpdateOperation query(Map<String, ?> query) {
		_query = adapter.toDBObject query
		this
	}
	
	DBUpdateOperation update(Map<String, ?> update) {
		_update = adapter.toDBObject update
		this
	}
	
	DBUpdateOperation fields(Map<String, ?> fields) {
		_fields = adapter.toDBObject fields
		this
	}
	
	DBUpdateOperation sort(Map<String, ?> sort) {
		_sort = adapter.toDBObject sort
		this
	}
	
	DBUpdateOperation multi(boolean multi) {
		_multi = multi
		this
	}
	
	DBUpdateOperation upsert(boolean upsert) {
		_upsert = upsert
		this
	}
	
	DBUpdateOperation returnNew(boolean returnNew) {
		_returnNew = returnNew
		this
	}
	
	DBUpdateOperation remove(boolean remove) {
		_remove = remove
		this
	}
	
	Map findAndModify() {
		DBObject result = gmongo.getDB(_database)[_collection].findAndModify(_query, _fields, _sort, _remove, _update, _returnNew, _upsert)
		return adapter.toMap(result)
	}
	
	WriteResult update() {
		return gmongo.getDB(_database)[_collection].update(query, _update, _upsert, _multi)
	}
}
