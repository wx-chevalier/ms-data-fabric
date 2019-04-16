FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "-Dfile.encoding=UTF8", "-Duser.timezone=GMT+08", \
           "/app.jar", "--spring.config.location=/app/config/application.yml" \
]
