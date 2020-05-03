package com.tsys.sre.kafka.consumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import java.util.concurrent.ExecutionException;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;

import java.util.Properties;

public class JmeterKafkaProducer extends AbstractJavaSamplerClient {
	Logger logger=getNewLogger();
	Producer<Long, String> producer ;
	public static String KAFKA_BROKERS = "localhost:9092";
	//public static String CLIENT_ID="vk";
	public static String TOPIC_NAME="demoTopic";
	public static String GROUP_ID_CONFIG="consumerGroup1";
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
	public static String OFFSET_RESET_LATEST="latest";
	public static String OFFSET_RESET_EARLIER="earliest";
	
	public static Integer MAX_POLL_RECORDS=1;
	
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("KAFKA_BROKERS", "localhost:9092");
		//defaultParameters.addArgument("CLIENT_ID", "vk");
		defaultParameters.addArgument("TOPIC_NAME", "demoTopic");
		defaultParameters.addArgument("OFFSET_RESET_LATEST", "latest");
		defaultParameters.addArgument("OFFSET_RESET_EARLIER", "earliest");
		defaultParameters.addArgument("MESSAGE", "");
		
		return defaultParameters;
	}
	public void setupTest(JavaSamplerContext context) {
		logger.warn("### [THREAD - "+context.getJMeterContext().getThreadNum()+"] Starting test now. Loading configuration !");
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, context.getParameter("KAFKA_BROKERS", KAFKA_BROKERS));
		//props.put(ProducerConfig.CLIENT_ID_CONFIG, context.getParameter("CLIENT_ID", CLIENT_ID));
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
		
		MAX_NO_MESSAGE_FOUND_COUNT = Integer.valueOf(context.getParameter("MAX_NO_MESSAGE_FOUND_COUNT", MAX_NO_MESSAGE_FOUND_COUNT.toString()));
		MAX_POLL_RECORDS = Integer.valueOf(context.getParameter("MAX_POLL_RECORDS", MAX_POLL_RECORDS.toString()));
		
		TOPIC_NAME = context.getParameter("TOPIC_NAME", TOPIC_NAME);
		GROUP_ID_CONFIG = context.getParameter("GROUP_ID_CONFIG", GROUP_ID_CONFIG);
		OFFSET_RESET_LATEST = context.getParameter("OFFSET_RESET_LATEST", OFFSET_RESET_LATEST);
		OFFSET_RESET_EARLIER = context.getParameter("OFFSET_RESET_EARLIER", OFFSET_RESET_EARLIER);
		
		producer= new KafkaProducer<>(props);
		logger.warn("### [THREAD - "+context.getJMeterContext().getThreadNum()+"]Starting test now. Configurations loaded !");
	}
	public void teardownTest(JavaSamplerContext context) {
		logger.warn("### [THREAD - "+context.getJMeterContext().getThreadNum()+"] Test completed. !");
	}
	@Override
	public SampleResult runTest(JavaSamplerContext context) {

		String message= context.getParameter("MESSAGE", "This is default message from JMeter. Check if you assign values fo MESSAGE property");
		SampleResult result = new SampleResult();
		boolean success = true;
		result.sampleStart();
		//
		// Write your test code here.
		ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(TOPIC_NAME,
				message);
		try {
			RecordMetadata metadata = producer.send(record).get();

			logger.warn("### [THREAD - "+context.getJMeterContext().getThreadNum()+"]Record sent with value '" + message + "', to partition " + metadata.partition()
				+ " with offset " + metadata.offset());

		} 
		catch (ExecutionException e) {
			logger.error("### [THREAD - "+context.getJMeterContext().getThreadNum()+"]Error in sending record to kafka - "+e.getStackTrace());
		} 
		catch (InterruptedException e) {
			logger.error("### [THREAD - "+context.getJMeterContext().getThreadNum()+"]Error in sending record to kafka - "+e.getStackTrace());
		}
		//
		result.setResponseMessage(message);
		result.sampleEnd();
		result.setSuccessful(success);
		return result;
	}

}