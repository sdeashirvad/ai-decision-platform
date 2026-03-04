# AI Decision Platform

This is a Spring Boot application that provides an AI-powered decision platform.

## Docker Build Instructions

To build and run the application using Docker, follow these steps:

1.  **Build the application**:
    ```bash
    ./gradlew clean bootJar
    ```

2.  **Build the Docker image**:
    ```bash
    docker build -t sdeashirvad/ai-decision-platform:latest .
    ```

3.  **Run the Docker container**:
    ```bash
    docker run -p 8080:8080 --env-file .env sdeashirvad/ai-decision-platform:latest
    ```

4.  **Pull the Docker image from Docker Hub**:
    ```bash
    docker pull sdeashirvad/ai-decision-platform:latest
    ```

5.  **Run the Docker container in detached mode**:
    ```bash
    docker run -d -p 8080:8080 --env-file .env sdeashirvad/ai-decision-platform:latest
    ```

6.  **Push the Docker image to Docker Hub**:
    ```bash
    docker push sdeashirvad/ai-decision-platform:latest
    ```
