FROM amazoncorretto:21 as builder

WORKDIR app
COPY . .

RUN ./gradlew clean bootJar

FROM amazoncorretto:21

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["/app.jar"]