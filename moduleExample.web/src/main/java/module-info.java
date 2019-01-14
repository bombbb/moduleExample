module wzp.moduleExample.web {
	requires transitive wzp.moduleExample.util;

	requires java.sql;
	requires java.xml;
	requires java.annotation;
	requires java.instrument;
	requires jdk.unsupported;
	
	// jar包的自动模块化
	requires transitive guava;
	requires transitive fastjson;
	requires transitive tomcat.embed.core;
	requires transitive slf4j.api;
	requires transitive logstash.logback.encoder;
	requires transitive tomcat.jdbc;
	requires transitive mybatis;
	requires transitive mybatis.spring;
	requires transitive pagehelper;
	requires transitive reactor.core;
	requires transitive mongo.java.driver;
	
	requires transitive org.reactivestreams;
	requires transitive spring.core;
	requires transitive spring.beans;
	requires transitive spring.context;
	requires transitive spring.data.elasticsearch;
	requires transitive spring.data.redis;

	requires spring.jdbc;
	requires spring.tx;
	requires spring.aop;
	requires spring.web;
	requires spring.expression;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires spring.boot.starter.web;
	


	exports wzp.moduleExample.web.boot;
	exports wzp.moduleExample.web.controller;
	exports wzp.moduleExample.web.service;

	// 通过反射来注入@Autowired注解的field
	opens wzp.moduleExample.web.boot;
	opens wzp.moduleExample.web.controller;
	opens wzp.moduleExample.web.service;

	// 让spring找得到classpath下spring文件夹内的配置文件
	opens spring;
}