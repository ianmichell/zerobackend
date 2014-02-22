package com.zerobackend.ops

import com.gmongo.GMongo
import com.mongodb.DBObject
import com.mongodb.WriteResult
import com.zerobackend.adapter.JsonToDBObjectAdapter

class DBInsertOperation {
	
	private JsonToDBObjectAdapter adapter
	private GMongo gmongo
	private String _database
	private String _collection
	private List<DBObject> _objects = []

	public DBInsertOperation(JsonToDBObjectAdapter adapter, GMongo mongo) {
		gmongo = mongo
		this.adapter = adapter
	}
	
	DBInsertOperation database(String database) {
		_database = database
		this
	}
	
	DBInsertOperation collection(String collection) {
		_collection = collection
		this
	}
	
	DBInsertOperation object(List<Map<String, ?>> object) {
		_objects = object.collect { 
			adapter.toDBObject it
		}
		this
	}
	
	DBInsertOperation object(Map<String, ?> object) {
		_objects.add adapter.toDBObject(object)
		this
	}
	
	WriteResult insert() {
		gmongo.getDB(_database)[_collection] << _objects
	}
}
