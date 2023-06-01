FROM ubuntu:latest

# Install Java 19
RUN apt-get update && \
    apt-get install -y openjdk-19-jre-headless && \
    rm -rf /var/lib/apt/lists/*

# Create a directory for the application
RUN mkdir /app

# Copy the Java Jar file to the container
COPY mockserver-1.0.jar /app/

# Copy the JSON config file to the container
COPY config.json /app/

# Set the working directory to the application directory
WORKDIR /app

# Set the CONFIG_PATH environment variable to point to the JSON config file
ENV CONFIG_PATH /app/config.json

# Start the application with the config file
CMD ["java", "-jar", "mockserver-1.0.jar", "--config-file", "$CONFIG_PATH"]