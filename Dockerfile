FROM openjdk:17-alpine

COPY target/auth-0.0.1-SNAPSHOT.jar /app/auth.jar

WORKDIR /app

EXPOSE 8081

CMD ["java", "-jar", "auth.jar"]
