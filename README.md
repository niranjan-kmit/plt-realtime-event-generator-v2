# plt-realtime-event-generator
Java application for generatng events

Step 1. Dowload and Install the Apache Kafka latest version.

Step2 : Set Environment Variables for Kafka

Path: D:\kafka_2.12-2.4.0\bin

Step 3: Open command line in your Kafka installation folder.

Step 4: Lunch Zookeeper with bin\windows\zookeeper-server-start.bat config\zookeeper.properties

Step 5:Open a second command line in your Kafka installation folder

Step 6:Launch single Kafka broker: bin\windows\kafka-server-start.bat config\server.properties

Now the Kafka Server Started with Sinle Broker



Now we are going to run the Spring Boot Application which Interact with Kafka to feed data to Kafa Topic.

Step 1. Maven need to be configured in the machine.

Step2: Open Command Spring Boot Application Root folder

Step3: run the command mvn spring-boot:run

Spring Boot Application Start Running in the local machine.

Start a console consumer for that topic: bin\windows\kafka-console-consumer.bat --zookeeper localhost:2181 --topic feed-topic--from-beginning
