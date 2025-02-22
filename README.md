# Project Overview

This project contains the following repositories:

1. **procmatrix-core**: Contains library classes. DB & Auth. details can be found at  application-core.properties
2. **procmatrix**: Contains a Spring Boot application to provide matrix functionalities. please refer Link procmatrix/README.md
3. **procmatrix-rotation**: Contains matrix rotation functionality. please refer Link  procmatrix-rotation/README.md
4. **procmatrix-client**: Contains a client utility app to access APIs. please refer Link procmatrix-clientREADME.md
   
Below are the high level components for local system:

   ![alt text](https://github.com/Rahulsawant/qodana-procmatrix/blob/master/docs/procmatrix.png)


## Steps to Run the Applications

1. Clone the repository:
    ```sh
    git clone <repository-url>
    ```
2. Give permission to the build_and_run.sh script:
    ```sh
    chmod +x build_and_run.sh
    ```
4. Run the script:
    ```sh
    ./build_and_run.sh
    ```
5. alternately, we can run each service separately using 'mvn spring-boot:run'


## build_and_run.sh Script

1. Builds all projects using Maven.
2. Runs all the applications as separate processes so that we can access the APIs from the procmatrix-client application:
    - **procmatrix** application runs on port 8080.
    - **procmatrix-rotation** application runs on port 8081.
    - **procmatrix-client** application runs on port 8082.
3. Spring Boot applications will also run and use the H2 database and cache.
   Note- h2 DB file 'matrixdb' is created at user root directory & shared by both the services, AUTO_SERVER=TRUE mode works fine windows.
   Changes for shared db can be done at application-core.properties (spring.datasource.url)

## Accessing the APIs

You can access the APIs either via an API tool (e.g., Postman) or using the procmatrix-client application. Basic Auth is enabled, so you need to provide credentials to access the APIs.

## Design Considerations

### Database

- **H2 Database**: Used for lightweight and local testing. We can use any other database like Cassandra based on the requirements and load for scalability.
- **Shared Database**: The database is shared between services to help with creation and running separate microservices.

### Caching

- **In-Memory Caching**: Added to store the matrix data for faster access.
- **Shared Caching**: We can use shared caching between all our microservices like Redis, Hazelcast, etc., for better performance.

### Endpoints

- Services are running on different ports to access them separately for testing and local usage.
- We can use an API gateway to access them via a single port along with load balancing.

### Security

- **Basic Auth**: Added for security to help validate access and local run. Details are in application-core.properties.
- We can use OAuth2 for more secure access and token-based support.

### Scaling and Performance

- Created separate services for each functionality to help with scaling and maintenance.
- We can use Kubernetes-based microservices for better scaling and management with functionalities like HPA, load balancing, etc.

### Rotation API

- When a user sends a matrix via a POST call, we store it in the database and cache it for future rotations.
- When a user sends a matrix ID via a GET call, we retrieve the matrix from the database and cache it for future rotations.

#### Considerations

- We're rotating the original matrix but not storing it to prevent redundancy. With multiple requests, storing the same matrix after a 360-degree rotation could lead to duplicate data, consuming more resources for comparison and deduplication.

#### For the Future

- We can cache the rotated matrix with its ID, rotation angle, and the rotated matrix itself. If a user requests the same rotation again, we can retrieve it from the cache. Otherwise, we'll perform the rotation operation.
- Alternatively, we could store the data in a separate table, including the matrix ID, rotation angle, and the rotated matrix data.
- For very large data, we can add multipart file implementation to handle the data.

### Addition API

- When a user requests addition via a GET call with IDs, we add matrices and return the result. We don't cache the result as it's not required.
- When a user requests addition via a POST call, we add two matrices and return the result. We don't store the new matrix in the database.

#### Considerations

- We're not storing the added matrix, as it is less likely to be used again. Storing it would consume more resources for comparison and deduplication.

#### For the Future

- If there is a business use case where we receive additions for the same matrices, we can store them in the cache or database, considering the effort required for comparison and deduplication.
