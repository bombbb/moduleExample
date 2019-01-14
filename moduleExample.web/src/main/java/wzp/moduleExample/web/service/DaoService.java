package wzp.moduleExample.web.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import wzp.moduleExample.util.mybatis.mapper.PipelineMapper;
import wzp.moduleExample.util.mybatis.mapper.UserMapper;

import wzp.moduleExample.util.mybatis.bo.User;

@Service
public class DaoService {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	PipelineMapper pipelineMapper;
	@Autowired
	@Qualifier("txManager")
	PlatformTransactionManager txManager;

	private DaoService self;

	@PostConstruct
	private void init() {
		self = context.getBean(DaoService.class);
	}

	@Transactional(transactionManager = "txManager", propagation = Propagation.REQUIRES_NEW)
	public long getNextSeq() {
//		User user = userMapper.getUser(33);
//		user.setUserName(user.getUserName() + "1");
//		userMapper.updateUser(user);

		String name = "default";
		pipelineMapper.setNextSeq(name);
		return pipelineMapper.getCurrentSeq(name);

	}

	@Transactional(transactionManager = "txManager", propagation = Propagation.REQUIRED)
	public long updateUser() {
		User user = userMapper.getUser(43);
		user.setUserName(user.getUserName() + "1");
		userMapper.updateUser(user);
		Long x = self.getNextSeq();

		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return x;
	}

}
