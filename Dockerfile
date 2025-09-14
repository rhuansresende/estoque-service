# Stage 1: build
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn -B -f pom.xml -e -DskipTests dependency:go-offline
COPY src src
RUN mvn -B -DskipTests package


# Stage 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/app/app.jar"]