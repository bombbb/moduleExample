package wzp.moduleExample.web.boot;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import wzp.moduleExample.util.mybatis.mapper.UserMapper;

import wzp.moduleExample.util.mybatis.bo.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { wzp.moduleExample.web.boot.Starter.class,
		wzp.moduleExample.web.boot.ApplicationConfig.class })
@Transactional
@AutoConfigureMockMvc
public class ApplicationTests {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private MockMvc mvc;

	@Test
	@Rollback
	public void testMybatis() {
		PageHelper.startPage(1, 5);
		List<User> users = userMapper.getUsers();
		assert (users.size() <= 5) : "每页数据量应该不大于5";
		try (final Page<User> page = (Page<User>) users) {
			assert (page.getTotal() > 100) : "总用户应该要大于100人";
		}

		User user = users.get(0);
		int id = user.getUser_id();
		String name = user.getUserName() + "1";
		user.setUserName(user.getUserName() + "1");
		userMapper.updateUser(user);

		user = userMapper.getUser(id);
		assertEquals(name, user.getUserName());
	}

	@Test
	public void testMvc() throws Exception {
		mvc.perform(get("/info.do").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk());
	}

}
