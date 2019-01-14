package wzp.moduleExample.util.mongo.factory;

import java.util.concurrent.TimeUnit;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

//初始化bean的时候，顺便使注解生效，扫描所有Reactive Repository
@EnableReactiveMongoRepositories(basePackages = "wzp.moduleExample.util.mongo.repository.reactive", reactiveMongoTemplateRef = "mongoReactiveTemplate")
public class MongoReactiveClientFactory implements FactoryBean<MongoClient>, DisposableBean {
	// 标准连接地址mongodb://test:test@192.168.3.11:27017/?authSource=test
	@Value("${mongo.uri}")
	private String mongoUri;

	private MongoClient client;

	@Override
	public MongoClient getObject() throws Exception {
		if (this.client != null) {
			return this.client;
		}
		// 对POJO进行BASE格式的序列化和反序列化
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
				com.mongodb.MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		// 连接池最小连接数为1，最大连接数为100，3秒内未获取空闲连接则直接失败；
		// 建立连接时，3秒超时；发起查询时，10秒超时
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(mongoUri))
				.applyToConnectionPoolSettings(
						builder -> builder.minSize(1).maxSize(100).maxWaitTime(3000, TimeUnit.MILLISECONDS).build())
				.applyToSocketSettings(builder -> builder.connectTimeout(3000, TimeUnit.MILLISECONDS)
						.readTimeout(10000, TimeUnit.MILLISECONDS).build())
				.codecRegistry(pojoCodecRegistry).readConcern(ReadConcern.MAJORITY).writeConcern(WriteConcern.MAJORITY)
				.build();
		this.client = MongoClients.create(settings);
		return this.client;
	}

	@Override
	public Class<?> getObjectType() {
		return MongoClient.class;
	}

	@Override
	public void destroy() throws Exception {
		if (this.client != null) {
			this.client.close();
		}
	}
}
