FROM maven:3.6.0-jdk-11-slim AS MAVEN_BUILD
COPY pom.xml /build/
COPY Service /build/Service/
COPY Commons /build/Commons/
COPY FunctionalTests /build/FunctionalTests/
WORKDIR /build/
RUN mvn package
FROM openjdk:11.0-jre
WORKDIR /app
COPY --from=MAVEN_BUILD /build/Service/target/performancereview-service-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "performancereview-service-0.0.1-SNAPSHOT.jar"]
