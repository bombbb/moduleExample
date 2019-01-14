package wzp.moduleExample.web.boot;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class,
		RedisReactiveAutoConfiguration.class, JmxAutoConfiguration.class, InfluxDbAutoConfiguration.class,
		ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class,
		ElasticsearchRepositoriesAutoConfiguration.class, DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, XADataSourceAutoConfiguration.class,
		JndiDataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class, TransactionAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
		MongoRepositoriesAutoConfiguration.class, MongoReactiveAutoConfiguration.class,
		MongoReactiveDataAutoConfiguration.class, MongoReactiveRepositoriesAutoConfiguration.class,
		WebClientAutoConfiguration.class, RestTemplateAutoConfiguration.class })
@EnableConfigurationProperties
//@EnableFeignClients("wzp.feign")
@ImportResource({ "classpath:/spring/applicationContext.xml" })
@ComponentScan({ "wzp.moduleExample.web.boot", "wzp.moduleExample.web.controller",
		"wzp.moduleExample.web.service" })
public class Starter {

	public static void main(String[] args) {
		try {
			System.setProperty("es.set.netty.runtime.available.processors", "false");
			System.setProperty("spring.cloud.bootstrap.enabled", "false");
			SpringApplication app = new SpringApplication(Starter.class);
			app.run(args);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}