FROM openjdk:17
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} mysqldriverspringbootapi.jar
ENTRYPOINT ["java", "-jar", "build/libs/MySQLDriverSpringBootApi-0.0.1-SNAPSHOT.jar"]

