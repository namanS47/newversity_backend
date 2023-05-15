# Alpine Linux with OpenJDK JRE
FROM openjdk:17-jdk-slim
# Copy jar file
COPY newversity.jar /newversity.jar

# run the app
CMD ["java", "-jar", "/newversity.jar"]