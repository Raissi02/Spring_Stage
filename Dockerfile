# ---- Etape 1 : compilation avec Maven et JDK 21 ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Cache des dependances
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Compilation de l'application (tests executes dans le pipeline, pas ici)
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Etape 2 : image d'execution legere (JRE 21) ----
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
