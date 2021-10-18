package com.lietsoft.TestEmbeddedKafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class TestEmbeddedKafkaApplicationTests {

	@Autowired
	private KafkaConsumer consumer;

	@Autowired
	private KafkaProducer producer;

	@Value("${test.topic}")
	private String topic;

	@Test
	void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived() throws Exception {
		producer.send(topic, "Sending with own simple KafkaProducer");
		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

		Assertions.assertEquals(consumer.getLatch().getCount(), 0L);
		Assertions.assertTrue(consumer.getPayload().contains("embedded-test-topic"));
	}

}
