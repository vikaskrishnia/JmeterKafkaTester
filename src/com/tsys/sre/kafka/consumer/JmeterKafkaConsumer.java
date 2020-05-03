package com.tsys.sre.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class JmeterKafkaConsumer extends AbstractJavaSamplerClient {
	
	Logger logger=getNewLogger();
	Producer<Long, String> producer ;
	public static String KAFKA_BROKERS = "localhost:9092";
	//public static String CLIENT_ID="vk";
	public static String TOPIC_NAME="demoTopic";
	public static String GROUP_ID_CONFIG="consumerGroup1";
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
	public static String OFFSET_RESET_LATEST="latest";
	public static String OFFSET_RESET_EARLIER="earliest";
	public static Integer MAX_POLL_RECORDS=1000;	
	public static Integer POLL_DURATION=100;
	Consumer<Long, String> consumer ;

	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("KAFKA_BROKERS", "localhost:9092");
		defaultParameters.addArgument("TOPIC_NAME", "demoTopic");
		defaultParameters.addArgument("GROUP_ID_CONFIG", "consumerGroup1");
		defaultParameters.addArgument("MAX_POLL_RECORDS", "1000");
		defaultParameters.addArgument("POLL_DURATION", "100");
		defaultParameters.addArgument("MAX_NO_MESSAGE_FOUND_COUNT", "100");
		defaultParameters.addArgument("OFFSET_RESET_LATEST", "latest");
		defaultParameters.addArgument("OFFSET_RESET_EARLIER", "earliest");
		
		return defaultParameters;	
	}
	public void setupTest(JavaSamplerContext context) {
		logger.warn("### Starting test now. Loading configuration for thread "+context.getJMeterContext().getThreadNum());
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_RESET_EARLIER);
		
		MAX_NO_MESSAGE_FOUND_COUNT = Integer.valueOf(context.getParameter("MAX_NO_MESSAGE_FOUND_COUNT", MAX_NO_MESSAGE_FOUND_COUNT.toString()));
		MAX_POLL_RECORDS = Integer.valueOf(context.getParameter("MAX_POLL_RECORDS", MAX_POLL_RECORDS.toString()));
		
		TOPIC_NAME = context.getParameter("TOPIC_NAME", TOPIC_NAME);
		GROUP_ID_CONFIG = context.getParameter("GROUP_ID_CONFIG", GROUP_ID_CONFIG);
		OFFSET_RESET_LATEST = context.getParameter("OFFSET_RESET_LATEST", OFFSET_RESET_LATEST);
		OFFSET_RESET_EARLIER = context.getParameter("OFFSET_RESET_EARLIER", OFFSET_RESET_EARLIER);
		POLL_DURATION = Integer.valueOf(context.getParameter("POLL_DURATION", POLL_DURATION.toString()));
 		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(TOPIC_NAME));
		logger.warn("### Starting test now. Configuration loaded for thread "+context.getJMeterContext().getThreadNum());
	}
	public void teardownTest(JavaSamplerContext context) {
		logger.warn("### Closing consumer ..");
		consumer.close();
		logger.warn("### Consumer closed !");		
	}
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		boolean success = true;
		result.sampleStart();
		//
		// Write your test code here.
		int noMessageFound = 0;
		int counter=0;
		while (counter++<MAX_POLL_RECORDS) {
			ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofMillis(POLL_DURATION));
			// 1000 is the time in milliseconds consumer will wait if no record is found at broker.
			
			if (consumerRecords.count() == 0) {
				logger.warn("### [THREAD - "+context.getJMeterContext().getThreadNum()+"] Message not found. Incrementing counter - "+noMessageFound+"/"+MAX_NO_MESSAGE_FOUND_COUNT);
				noMessageFound++;
				if (noMessageFound > MAX_NO_MESSAGE_FOUND_COUNT)
				{
					// If no message found count is reached to threshold exit loop.  
					break;
				}
				else
					continue;
			}
			//print each record. 
			consumerRecords.forEach(record -> {
				logger.warn("### [THREAD - "+context.getJMeterContext().getThreadNum()+"]Kafka message fetched. Record Key: '" + record.key() +"', Record value: '" + record.value()+"', Record partition: '" + record.partition()+"', Record offset: '" + record.offset()+"'");
			});
			// commits the offset of record to broker. 
			consumer.commitAsync();
		}
				
		
		//
		result.sampleEnd();
		result.setSuccessful(success);
		return result;
	}

}