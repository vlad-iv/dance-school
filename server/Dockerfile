#### Stage 1: Build the application
FROM openjdk:11-jdk-slim as build

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B

# Copy UI
COPY webpack.config.js .
COPY webpack.generated.js .
# Install node
RUN ./mvnw com.github.eirslett:frontend-maven-plugin:1.7.6:install-node-and-npm -DnodeVersion="v12.13.0"

# Copy the project source
COPY src src
COPY Dockerfile .

ARG PORTAL_PORT=8600
ARG SPRING_LOG_LEVEL=INFO
ARG SPRING_LOG_PATTERN="%d{yyyy-MMM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{15} - %msg%n"

# Package the application
RUN ./mvnw package -DskipTests -Pprod
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM openjdk:11-jre-slim

ARG DEPENDENCY=/app/target/dependency
ARG PORTAL_PORT=8600
ARG SPRING_LOG_LEVEL=INFO
ARG SPRING_LOG_PATTERN="%d{yyyy-MMM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{15} - %msg%n"

ENV PORTAL_PORT=${PORTAL_PORT}
ENV SPRING_LOG_LEVEL=${SPRING_LOG_LEVEL}
ENV SPRING_LOG_PATTERN=${SPRING_LOG_PATTERN}

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","app.Application"]
