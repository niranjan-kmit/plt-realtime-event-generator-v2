# plt-realtime-event-generator using Docker Compose.
Java application for generatng events

1. Open the terminal in the project root folder type the command ==> docker-compose up

2. EveneGenerator App will start.Here is the few endpoints to start events

3. localhost:8080/startAutoGenerate/1000 => generate events every one second

4. localhost:8080/startAutoGenerate/-1   => generates events continually

5. localhost:8080/stopAutoGenerate   => stop the event generation.