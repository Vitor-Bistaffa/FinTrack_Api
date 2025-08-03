# Etapa de build
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia apenas arquivos de build primeiro (para cache das dependências)
COPY pom.xml ./
COPY .mvn .mvn
RUN mvn dependency:go-offline

# Copia o resto do código depois\
COPY src ./src

# Compila o projeto (sem testes)
RUN mvn package -DskipTests

# Etapa de execução
FROM openjdk:21

COPY --from=build /app/target/FinTrack_Api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app.jar"]
