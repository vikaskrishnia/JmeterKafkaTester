# JmeterKafkaTester
Test Kafka Producer and Kafka Consumer from Jmeter using "Java Sampler".

<b>Prerequisites</b>
Below are the prerequisites to setup Jmeter for kafka testing.
1.	Running Kafka Server is available along with broker details.
2.	Apache Jmeter (version 5 or above recommended)
3.	Add "kafka-clients-2.5.0.jar" to %JMETER_HOME%/lib/ext/
4.	kafka-jmeter-tester-<version>.jar copied in <JMETER_HOME>/lib/ext directory. Launch jmeter using jmeter.bat/jmeter.sh

Follow these steps to create your first test plan. You can also use the attached sample test plan for quick start.
1.	Create a test plan and add "Java Sampler" for Producer. 
2.	Select “com.tsys.sre.kafka.consumer.JmeterKafkaProducer” or “com.tsys.sre.kafka.consumer.JmeterKafkaConsumer” as required.

<b>ReadMe</b>

By default it should populate following parameters with default values. Ensure these are reviewed and updated as per the testing environment.

	KAFKA_BROKERS             	## Kafka Broker hostname and port number.    
	TOPIC_NAME                	## Topic Name to send and receive messages
	GROUP_ID_CONFIG           	## Consumer Group Name. Default is "consumerGroup1";
	MAX_NO_MESSAGE_FOUND_COUNT  	## Consumer will try to attempt retrieving messages from TOPIC_NAME. It will wait for POLL_DURATION milliseconds if no message is available to fetch. Consumer will retry fetching messages  MAX_NO_MESSAGE_FOUND_COUNT number of times and exit when this threshold is breached. Consumer will fetch maximum  MAX_POLL_RECORDS records from TOPIC_NAME;

	
<b>Initial setup</b>  

1. Download and copy "kafka-jmeter-tester-v1.jar" file to %JMETER_HOME%/lib/ext/
2. Add "kafka-clients-2.5.0.jar" to %JMETER_HOME%/lib/ext/
3. Run jmeter.bat to start Jmeter.
4. Add "Java Sampler" for Producer. 

