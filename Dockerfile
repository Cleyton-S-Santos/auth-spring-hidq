# Build stage
FROM maven:3.6.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app/pom.xml
RUN mvn -f /home/app/pom.xml clean package

# Package stage
FROM openjdk:17-alpine
COPY --from=build /home/app/target/auth-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]
