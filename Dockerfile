FROM openjdk:17
VOLUME /tmp
RUN gradle clean bootJar
RUN ls -l /build-workspace/build/libs/
ARG JAR_FILE
COPY ${JAR_FILE} MySQLDriverSpringBootApi.jar
ENTRYPOINT ["java", "-jar", "/MySQLDriverSpringBootApi.jar"]