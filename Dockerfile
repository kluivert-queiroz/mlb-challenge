FROM eclipse-temurin:17-jre-focal
WORKDIR /home/app
COPY build/libs/mlb-challenge-*-all.jar mlb-challenge.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dotel.java.global-autoconfigure.enabled=true", "-jar", "mlb-challenge.jar"]
