FROM openjdk:11-jdk-oraclelinux8
WORKDIR /app
CMD ./gradlew build
ADD build/libs/testTaskForAlphaBank-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]