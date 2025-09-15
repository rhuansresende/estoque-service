# ===========================
# Stage 1: Build com Maven
# ===========================
FROM maven:3.9.4-eclipse-temurin-11 AS build

# não precisa sobrescrever JAVA_HOME, já vem pronto nessa imagem
RUN java -version && mvn -version

WORKDIR /app

# Copia apenas o pom.xml primeiro (cache das dependências)
COPY pom.xml ./

RUN mvn -B -f pom.xml -e -DskipTests dependency:go-offline

# Copia o restante do código
COPY src src

RUN mvn -B -DskipTests clean package


# ===========================
# Stage 2: Runtime com JRE
# ===========================
FROM eclipse-temurin:11-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
