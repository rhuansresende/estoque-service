# ===========================
# Stage 1: Build com Maven
# ===========================
FROM maven:3.9.4-eclipse-temurin-11 AS build

# Garante que o JAVA_HOME está setado corretamente
ENV JAVA_HOME=/usr/local/openjdk-11
ENV PATH="${JAVA_HOME}/bin:${PATH}"

WORKDIR /app

# Copia apenas o pom.xml primeiro (para aproveitar cache das dependências)
COPY pom.xml ./

# Baixa dependências e coloca em cache
RUN mvn -B -f pom.xml -e -DskipTests dependency:go-offline

# Copia o restante do código fonte
COPY src src

# Faz o build do projeto (gera o JAR)
RUN mvn -B -DskipTests clean package


# ===========================
# Stage 2: Runtime com JRE
# ===========================
FROM eclipse-temurin:11-jre

WORKDIR /app

# Copia o JAR gerado do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do backend
EXPOSE 8000

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
