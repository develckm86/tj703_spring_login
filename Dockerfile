FROM --platform=linux/amd64 eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean
RUN ./gradlew build -x test


FROM eclipse-temurin:21-jdk
COPY --from=builder /app/build/libs/*.jar /app.jar
VOLUME /temp
#RUN java -jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#docker build --platform linux/amd64 -t develckm/boot-app:0.0.1 .
#docker push develckm/boot-app:0.0.1
#sudo docker run -d --rm --name bootApp -p 80:80 develckm/boot-app:0.0.5

