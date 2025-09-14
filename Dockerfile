# Stage 1: build
FROM maven:3.9.4-eclipse-temurin-11 AS build
WORKDIR /app

# Copia o pom e baixa dependências
COPY pom.xml ./
RUN mvn -B -f pom.xml -e -DskipTests dependency:go-offline

# Copia o código fonte e faz o build
COPY src src
RUN mvn -B -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copia o jar construído
COPY --from=build /app/target/*.jar app.jar

# Porta da aplicação
EXPOSE 8000

# Comando de inicialização
ENTRYPOINT ["java","-jar","/app/app.jar"]
