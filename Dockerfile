FROM --platform=linux/amd64 eclipse-temurin:21-jdk AS builder
#FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . /app
#gradlew 를 사용하기 위해서 복사
RUN chmod +x ./gradlew
RUN ./gradlew clean
RUN ./gradlew build -x test

FROM eclipse-temurin:21-jdk

VOLUME /temp
COPY --from=builder /app/build/libs/*.jar /app.jar

#RUN java -jar /app.jar --logging.level.root=ERROR --logging.file.name=/temp/app.log
ENTRYPOINT ["java","-jar","/app.jar","--logging.level.root=ERROR","--logging.file.name=/temp/app.log"]


#docker build --platform linux/amd64 -t develckm/spring-app:0.1.0 .
#docker buildx build --platform linux/amd64,linux/arm64 -t develckm/spring-app:0.0.2 .
#docker run -p 7777:7777 develckm/spring-app:0.0.2