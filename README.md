# JmeterKafkaTester
Test Kafka Producer and Kafka Consumer from Jmeter using "Java Sampler".

<b>Prerequisites</b>
1. Running Kafka Server.
2. Apache Jmeter (version 5 or above recommended)
3. kafka-jmeter-tester-<version>.jar copied in <JMETER_HOME>/lib/ext

<b>ReadMe</b>

Following parameters should be reviewed before running the tests.

	1. KAFKA_BROKERS             ## Kafka Broker hostname and port number.    
	2. TOPIC_NAME                ## Topic Name to send and recieve messages
    3. GROUP_ID_CONFIG           ## Consumer Group Name. Default is "consumerGroup1";
	4. MAX_NO_MESSAGE_FOUND_COUNT  ## Consumer will try to attempt retriving messages from TOPIC_NAME by POLLING and waiting for   POLL_DURATION milliseconds. Consumer will try MAX_NO_MESSAGE_FOUND_COUNT retrivels and exit when this threashold is breached. Consumer will fetch maximum  MAX_POLL_RECORDS records from TOPIC_NAME;
	
<b>Initial setup</b>  

1. Download and copy "kafka-jmeter-tester-v1.jar" file to %JMETER_HOME%/lib/ext/
2. Add "kafka-clients-2.5.0.jar" to %JMETER_HOME%/lib/ext/
3. Run jmeter.bat to start Jmeter.
4. Add "Java Sampler" for Producer. 

