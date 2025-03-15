FROM maven:3.8.6-amazoncorretto-17 as builder
WORKDIR /app
COPY . /app
RUN mvn -f /app/pom.xml clean package -Pprod -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]