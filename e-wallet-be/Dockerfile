FROM eclipse-temurin:17-jdk-alpine as builder

RUN addgroup -S e-wallet && adduser -S e-wallet-be -G e-wallet
USER e-wallet-be

WORKDIR /opt/e-wallet-be-build
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw -B clean verify
 
FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/e-wallet-be
EXPOSE 8080
COPY --from=builder /opt/e-wallet-be-build/target/e-wallet-be.jar /opt/e-wallet-be/e-wallet-be.jar
ENTRYPOINT ["java", "-jar", "/opt/e-wallet-be/e-wallet-be.jar" ]
