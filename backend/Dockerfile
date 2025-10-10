FROM maven:3.9.6-amazoncorretto-21-al2023 AS build
WORKDIR /home/app

COPY ./pom.xml /home/app/pom.xml
COPY ./src/main/java/com/shopverse/backend/ShopVerseApplication.java	/home/app/src/main/java/com/shopverse/backend/ShopVerseApplication.java
RUN mvn -f /home/app/pom.xml clean package

COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:21-jdk-slim
EXPOSE 5000
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]