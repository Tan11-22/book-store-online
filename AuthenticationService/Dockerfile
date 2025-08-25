# Stage 1: Build the application using a Maven container
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Run the Maven build command to create the executable .jar file
# We skip tests because they should have already run in a previous CI step
RUN mvn clean package -DskipTests


# Stage 2: Create the final, lightweight runtime image
FROM openjdk:17-jdk-slim

# Set the working directory for the runtime container
WORKDIR /app

# Copy the .jar file created in the 'build' stage
# IMPORTANT: Verify that this .jar file name matches the one in your /target directory
COPY --from=build /app/target/AuthenticationService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the Spring Boot application listens on
EXPOSE 3002

# The command that will be executed when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]