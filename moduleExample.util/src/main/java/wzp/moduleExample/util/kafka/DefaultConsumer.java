package wzp.moduleExample.util.kafka;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import wzp.moduleExample.util.mybatis.mapper.PipelineMapper;

import wzp.moduleExample.util.mybatis.bo.SvcLinkRestResponse;
import wzp.moduleExample.util.mybatis.bo.SvcLinkSql;

public class DefaultConsumer implements InitializingBean, DisposableBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsumer.class);
	private static String TOPIC_SVC_LINK_SQL = "svc_link_sql";
	private static String TOPIC_SVC_LINK_REST_RESPONSE = "svc_link_rest_response";

	private final AtomicBoolean closed = new AtomicBoolean(false);
	private KafkaConsumer<String, String> consumer = null;

	@Value("${kafka.bootstrap.servers}")
	private String servers;

	@Autowired
	private PipelineMapper pipelineMapper;

	private void dealConsumerRecords(ConsumerRecords<String, String> consumerRecords) {
		List<SvcLinkSql> sqlRecords = new ArrayList<>();
		List<SvcLinkRestResponse> restRecords = new ArrayList<>();
		for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			JSONObject json = JSON.parseObject(consumerRecord.value());

			LocalDateTime ldt = LocalDateTime.parse(json.getString("@timestamp"),
					DateTimeFormatter.ISO_OFFSET_DATE_TIME);

			if (consumerRecord.topic().equals(TOPIC_SVC_LINK_SQL)) {
				SvcLinkSql record = json.toJavaObject(SvcLinkSql.class);
				record.setTimestamp(Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant()));
				String sql = record.getSqlText();
				if (sql != null && sql.length() > 2000) {
					record.setSqlText(sql.substring(0, 2000));
				}
				String exception = record.getExceptionMessage();
				if (exception != null && exception.length() > 2000) {
					record.setExceptionMessage(exception.substring(0, 2000));
				}
				sqlRecords.add(record);
			} else if (consumerRecord.topic().equals(TOPIC_SVC_LINK_REST_RESPONSE)) {
				SvcLinkRestResponse record = json.toJavaObject(SvcLinkRestResponse.class);
				record.setTimestamp(Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant()));
				String url = record.getUrl();
				if (url != null && url.length() > 1000) {
					record.setUrl(url.substring(0, 1000));
				}
				String exception = record.getExceptionMessage();
				if (exception != null && exception.length() > 2000) {
					record.setExceptionMessage(exception.substring(0, 2000));
				}
				restRecords.add(record);
			}

		}
		if (sqlRecords.size() > 0) {
			pipelineMapper.insertSvcLinkSql(sqlRecords);
		}
		if (restRecords.size() > 0) {
			pipelineMapper.insertSvcLinkRestResponse(restRecords);
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(TOPIC_SVC_LINK_SQL, TOPIC_SVC_LINK_REST_RESPONSE));

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!closed.get()) {
					try {
						ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
						if (closed.get()) {
							return;
						}
						if (consumerRecords.isEmpty()) {
							continue;
						}
						dealConsumerRecords(consumerRecords);
						consumer.commitSync();
						LOGGER.info("kafka consume offset commited");
					} catch (Exception ex) {
						LOGGER.error("kafka consume fail", ex);
					}
				}
				consumer.close();
			}
		});
		thread.start();
	}

	@Override
	public void destroy() throws Exception {
		closed.set(true);
		consumer.wakeup();
	}

}