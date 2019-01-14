package wzp.moduleExample.web.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import wzp.moduleExample.util.mongo.repository.UserRepository;
import wzp.moduleExample.util.mybatis.mapper.UserMapper;

import reactor.core.publisher.Mono;
import wzp.moduleExample.web.service.DaoService;
import wzp.moduleExample.util.mybatis.bo.User;

@Controller
public class Example {
	private final Logger LOGGER = LoggerFactory.getLogger(Example.class);

	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ElasticsearchTemplate esTemplate;
	@Autowired
	UserRepository userRepository;
	@Autowired
	StringRedisTemplate redisTemplate;
	@Autowired
	private DaoService daoService;

	void mybatisTest() {
		// 分页插件使用示例
		PageHelper.startPage(1, 5);
		List<User> users = userMapper.getUsers();
		try (final Page<User> page = (Page<User>) users) {
			// 分页插件同时返回总数
			LOGGER.info("total user count: " + page.getTotal());
			for (User user : users) {
				LOGGER.info("user: " + user.getUser_loginname());
			}
		}

	}

	String redisTest() {
		int id = 43;
		String redisKey = "wzpTestUser" + id;
		return redisTemplate.opsForValue().get(redisKey);
	}

	void esTest() {
		// elasticSearchService.listFailRate(1);
		TimeZone zone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(zone);
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		format.setTimeZone(zone);

		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.DATE, -5);
		for (int i = 0; i < 30; i++) {
			calendar.add(Calendar.DATE, -1);
			esTemplate.deleteIndex("jr_svc_link-" + format.format(calendar.getTime()));
			esTemplate.deleteIndex("logstash-" + format.format(calendar.getTime()));
			esTemplate.deleteIndex("jr_front-" + format.format(calendar.getTime()));
		}
	}

	void mongoTest() {
		User query = new User();
		query.setUser_id(43);
		User user = userRepository.findOne(org.springframework.data.domain.Example.of(query)).get();
		int i = 0;
	}

	@GetMapping(value = "/info.do", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String info(HttpServletRequest request) throws Exception {
		
		// mybatisTest();
		// mongoTest();
		// redisTest();
		esTest();
		String path = request.getServletPath();
		return String.valueOf(daoService.updateUser());
	}
}
