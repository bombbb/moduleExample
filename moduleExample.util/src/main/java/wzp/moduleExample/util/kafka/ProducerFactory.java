package wzp.moduleExample.util.kafka;

import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;

public class ProducerFactory implements FactoryBean<Producer<String, String>>, DisposableBean {
	private Producer<String, String> instance = null;
	@Value("${kafka.bootstrap.servers}")
	private String servers;

	@Override
	public Producer<String, String> getObject() throws Exception {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 2);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1000);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		instance = new KafkaProducer<>(props);

		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		try {
			Field field = this.getClass().getDeclaredField("instance");
			Class<?> result = field.getType();
			return result;
		} catch (NoSuchFieldException | SecurityException e) {
			return Producer.class;
		}
	}

	@Override
	public void destroy() throws Exception {
		if (instance == null) {
			return;
		}
		instance.flush();
		instance.close();
		instance = null;
	}

}

