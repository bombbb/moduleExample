module wzp.moduleExample.util {
	requires java.xml;
	requires java.sql;
	
	// jar包的自动模块化
	requires transitive mybatis;
	requires transitive commons.pool2;
	requires transitive reactor.core;
	requires transitive mongo.java.driver;
	requires transitive mongodb.driver.reactivestreams;
	requires transitive p6spy;
	requires transitive spring.beans;
	requires transitive spring.core;
	requires transitive spring.context;
	requires transitive spring.data.commons;
	requires transitive spring.data.redis;
	requires transitive org.reactivestreams;
	requires transitive org.mongodb.driver.async.client;
	requires transitive spring.data.mongodb;
	requires transitive kafka.clients;
	requires transitive slf4j.api;
	requires transitive fastjson;
	

	exports wzp.moduleExample.util.mybatis.bo;
	exports wzp.moduleExample.util.mybatis.mapper;
	exports wzp.moduleExample.util.redis;
	exports wzp.moduleExample.util.mongo.factory;
	exports wzp.moduleExample.util.mongo.repository;
	exports wzp.moduleExample.util.mongo.repository.reactive;
	exports wzp.moduleExample.util.kafka;
	exports wzp.moduleExample.util.p6spy;
	
	opens wzp.moduleExample.util.mybatis.bo;
	opens wzp.moduleExample.util.mongo.factory;
	opens wzp.moduleExample.util.kafka;
}