FROM eclipse-temurin:21-jdk AS build

WORKDIR /api

COPY .mvn/ .mvn/
COPY mvnw pom.xml sonar-project.properties ./

# Create a settings.xml to override blocked http repositories with https mirrors
RUN mkdir -p ~/.m2 && \
    echo '<settings><mirrors>' \
    '<mirror><id>central-mirror</id><mirrorOf>releases.java.net,shapshots.java.net,jvnet-nexus-staging,netbeans</mirrorOf>' \
    '<url>https://repo1.maven.org/maven2</url></mirror>' \
    '</mirrors></settings>' > ~/.m2/settings.xml

RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src/ src/

RUN ./mvnw -DskipTests -Pprod clean package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /api/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
