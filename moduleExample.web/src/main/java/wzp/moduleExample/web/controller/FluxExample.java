package wzp.moduleExample.web.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import wzp.moduleExample.util.mongo.repository.reactive.UserReactiveRepository;

import reactor.core.publisher.Mono;
import wzp.moduleExample.util.mybatis.bo.User;

@Controller
public class FluxExample {
	private static final Logger LOGGER = LoggerFactory.getLogger(FluxExample.class);

	@Autowired
	private ReactiveStringRedisTemplate redisReactiveTemplate;
	@Autowired
	private UserReactiveRepository userReactiveRepository;

	Mono<User> mongoTest() {
		User query = new User();
		query.setUser_id(43);
		return userReactiveRepository.findOne(org.springframework.data.domain.Example.of(query)).log();
	}

	Mono<User> reactiveTest() {
		int id = 43;
		String redisKey = "wzpTestUser" + id;
		// 用于判断是否从redis获取到了缓存的对象
		List<User> cached = Lists.newArrayList();
		Mono<User> monoRedisGet = redisReactiveTemplate.opsForValue().get(redisKey).map(strUser -> {
			// 此段逻辑的处理线程，应与redis get start的线程不同
			LOGGER.info("reactiveTest : redis get map");
			User user = JSON.parseObject(strUser, User.class);
			cached.add(user);
			return user;
		}).log();

		// 当redis里不存在缓存时，才到mongo查询
		User query = new User();
		query.setUser_id(id);
		Mono<User> monoMongoQuery = monoRedisGet
				.switchIfEmpty(userReactiveRepository.findOne(org.springframework.data.domain.Example.of(query))).log();

		Mono<User> monoRedisSet = monoMongoQuery.flatMap(user -> {
			LOGGER.info("reactiveTest : mongo query flatmap");
			if (cached.size() == 0) {
				// 当redis里不存在缓存时，才写入缓存
				return redisReactiveTemplate.opsForValue()
						.set(redisKey, JSON.toJSONString(user), Duration.ofSeconds(10)).map(success -> {
							// 此段逻辑的处理线程，应与mongo query flatmap的线程不同
							LOGGER.info("reactiveTest : redis set map");
							return user;
						});
			}
			return Mono.just(user);
		}).log();
		// 把对Flux处理流的订阅操作交由WebFlux框架负责
		return monoRedisSet;
	}

	@GetMapping(value = "/flux.do")
	@ResponseBody
	public Mono<User> flux(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// return Mono.empty();
		// return mongoTest();
		return reactiveTest();
	}
}
