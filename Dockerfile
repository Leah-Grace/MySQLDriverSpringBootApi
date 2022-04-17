FROM openjdk:17
COPY . /src/main/java/com.LeahGrace/MySQLDriverSpringBootApi
WORKDIR /src/main/java/com.LeahGrace/MySQLDriverSpringBootApi
RUN javac MySqlDriverSpringBootApiApplication.java
CMD ["MySqlDriverSpringBootApiApplication", "Main"]
