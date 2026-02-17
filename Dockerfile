FROM eclipse-temurin:17-jdk AS build

WORKDIR /api

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src/ src/

RUN ./mvnw -DskipTests -Pprod clean package

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /api/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
