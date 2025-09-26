FROM openjdk:21

COPY target/auth-svc.jar auth-svc.jar

ENTRYPOINT ["java", "-jar", "auth-svc.jar"]

EXPOSE 8080