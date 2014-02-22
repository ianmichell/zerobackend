package com.zerobackend.controller

import java.util.concurrent.Callable

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import com.mongodb.CommandResult
import com.mongodb.DBObject
import com.mongodb.WriteResult
import com.zerobackend.controller.conversion.JsonPropertyEditor
import com.zerobackend.model.Cursor
import com.zerobackend.services.DatabaseService

/**
 * This is just a basic query controller for now... We need to make it full asynchronous and add security + user meta data support
 * @author ian
 *
 */
@RestController
@RequestMapping("/db")
class DatabaseController {

	@Autowired
	DatabaseService databaseService

	@Autowired
	JsonPropertyEditor jsonConverter

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// we can bind some validations etc here!
		binder.registerCustomEditor(Map.class, jsonConverter)
	}

	@RequestMapping(value="/{database}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<Set<String>> showCollections(@PathVariable("database") String database) {
		return new Callable<Set<String>>() {
			@Override
			public Set<String> call() {
				databaseService.collectionNames database
			}
		}
	}

	@RequestMapping(value="/{database}/stats", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<CommandResult> databaseInfo(@PathVariable("database") String database) {
		return new Callable<CommandResult>() {
			@Override
			public CommandResult call() {
				databaseService.stats database
			}
		}
	}

	@RequestMapping(value="/{database}/{collection}/stats", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<Map> collectionInfo(@PathVariable("database") String database, @PathVariable("collection") String collection) {
		return new Callable<CommandResult>() {
			@Override
			public CommandResult call() {
				databaseService.stats database, collection
			}
		}
	}

	@RequestMapping(value="/{database}/{collection}/find", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<Cursor> find(@PathVariable("database") String d, @PathVariable("collection") c,
			@RequestParam(value="query", defaultValue="{}") Map<String, ?> q,
			@RequestParam(value="fields", defaultValue="{}") Map<String, ?> f, @RequestParam(value="sort", defaultValue="{}") Map<String, ?> s,
			@RequestParam(value="skip", defaultValue="0") int offset, @RequestParam(value="limit", defaultValue="100") int max) {

		return new Callable<Cursor>() {
			@Override
			public Cursor call() {
				databaseService.find {
					database d
					collection c
					query q
					fields f
					sort s
					skip offset
					limit max
				}
			}
		}
	}

	@RequestMapping(value="/{database}/{collection}/findOne", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<Map> findOne(@PathVariable("database") String d, @PathVariable("collection") c,
			@RequestParam(value="query", defaultValue="{}") Map<String, ?> q) {

		return new Callable<Map>() {
			@Override
			public Map call() {
				databaseService.findOne {
					database d
					collection c 
					query q 
				}
			}
		}
	}

	@RequestMapping(value="/{database}/{collection}/{documentId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<Map> get(@PathVariable("database") String d, @PathVariable("collection") String c, @PathVariable("documentId") String id) {
		return new Callable<Map>() {
			@Override
			public Map call() {
				databaseService.findOne {
					database d
					collection c
					query(["_id": id])
				}
			}
		}
	}
	
	@RequestMapping(value="/{database}/{collection}/insert", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<WriteResult> insert(@PathVariable("database") String d, @PathVariable("collection") String c, @RequestBody Object o) {
		return new Callable<WriteResult>() {
			@Override
			public WriteResult call() {
				databaseService.insert {
					database d
					collection c
					object o
				}
			}
		}
	}
	
	@RequestMapping(value="/{database}/{collection}/update", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<WriteResult> update(@PathVariable("database") String d, @PathVariable("collection") String c, @RequestBody Map<String, ?> u) {
		return new Callable<WriteResult>() {
			@Override
			public WriteResult call() {
				return databaseService.update {
					database d
					collection c
					query u.query ?: [:]
					update u.update
					if (u.containsKey("multi")) {
						multi u.multi
					}
					if (u.containsKey("upsert")) {
						upsert u.upsert
					}
				}
			}
		}
	}
	
	@RequestMapping(value="/{database}/{collection}/findAndModify", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Callable<Map> findAndModify(@PathVariable("database") String d, @PathVariable("collection") String c, @RequestBody Map<String, ?> u) {
		return new Callable<Map>() {
			@Override
			public Map call() {
				return databaseService.findAndModify {
					database d
					collection c
					query u.query ?: [:]
					sort u.sort
					update u.update
					if (u.containsKey("remove")) {
						remove u.remove
					}
					if (u.containsKey("returnNew")) {
						returnNew u.returnNew
					}
					if (u.containsKey("upsert")) {
						upsert u.upsert
					}
				}
			}
		}
	}
}
