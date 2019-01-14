package wzp.moduleExample.web.boot;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.pagehelper.PageInterceptor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;

@Configuration
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync(proxyTargetClass = true)
public class ApplicationConfig implements EnvironmentAware, ApplicationContextAware {
	private final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

	@Value("${mongo.uri}")
	private String mongoUri;
	private Environment env;
	private ApplicationContext context;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	// /////////////////////////////////////////////
	// mysql/////////////////////////////////////////
	// /////////////////////////////////////////////

	@Bean(name = "dataSource", destroyMethod = "close")
	@Primary
	@ConfigurationProperties(prefix = "tomcat.datasource")
	public DataSource primaryDataSource() {
		// 自动把带tomcat.datasource前缀的配置项，注入生成的datasource bean。
		// DataSourceBuilder找到三种datasource的类后，自动创建对象："com.zaxxer.hikari.HikariDataSource","org.apache.tomcat.jdbc.pool.DataSource","org.apache.commons.dbcp2.BasicDataSource"
		return DataSourceBuilder.create().build();
	}

	@Bean("txManager")
	@Primary
	public PlatformTransactionManager txManager(DataSource datasource) {
		DataSourceTransactionManager manager = new DataSourceTransactionManager();
		manager.setDataSource(datasource);
		return manager;
	}

	@Bean("ibatisSqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory(DataSource datasource, PlatformTransactionManager manager)
			throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(datasource);

		// 分页插件
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("helperDialect", "mysql");
		properties.setProperty("rowBoundsWithCount", "true");
		pageInterceptor.setProperties(properties);
		sessionFactory.setPlugins(new Interceptor[] { pageInterceptor });
		return sessionFactory.getObject();
	}

	@Bean
	public static BeanDefinitionRegistryPostProcessor mapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("wzp.moduleExample.util.mybatis.mapper");
		configurer.setSqlSessionFactoryBeanName("ibatisSqlSessionFactory");
		return configurer;
	}

	// /////////////////////////////////////////////
	// mongo/////////////////////////////////////////
	// /////////////////////////////////////////////
	@Bean("mongoClient")
	@Primary
	public MongoClient mongoClient() {
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		// 连接池最小连接数为1，最大连接数为100，3秒内未获取空闲连接则直接失败；
		// 建立连接时，3秒超时；发起查询时，10秒超时
		MongoClientOptions.Builder builder = MongoClientOptions.builder().minConnectionsPerHost(1)
				.connectionsPerHost(100).maxWaitTime(3000).connectTimeout(3000).socketTimeout(10000)
				.codecRegistry(pojoCodecRegistry).readConcern(ReadConcern.MAJORITY).writeConcern(WriteConcern.MAJORITY);
		MongoClientURI uri = new MongoClientURI(mongoUri, builder);

		return new MongoClient(uri);
	}

}
