FROM maven:3.6.0-jdk-11-slim AS MAVEN_BUILD
MAINTAINER Brian Hannaway
COPY pom.xml /build/
COPY Service /build/Service/
COPY Commons /build/Commons/
COPY FunctionalTests /build/FunctionalTests/
WORKDIR /build/
RUN mvn package
FROM openjdk:10-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/service/target/performancereview-service-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "performancereview-service-0.1.0.jar"]
