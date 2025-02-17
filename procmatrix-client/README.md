### procmatrix-client

The procmatrix-client provides an abstraction to call procmatrix APIs. 
- we're building a client application to interact with the procmatrix and procmatrix-rotation APIs using openApi yaml configurations 
- It is a command-line application that offers a menu to interact with the procmatrix APIs with default authentication added

## Build and Run the Project

1. Navigate to the procmatrix-client directory:
    ```sh
    cd procmatrix-client
    ```
2. Build the project using Maven:
    ```sh
    mvn clean install
    ```
3. Run the Spring Boot application:
    ```sh
    mvn spring-boot:run
    ```

## Functionalities
   Below yaml files contains specification to call procmatrix & procmatrix-rotation apis, we auto generate client code using openApi generator

    ```sh
      resources/procmatrix-apis.yaml
      resources/procmatrix-rotation.yaml
    ```


Choose an API to invoke:

1. Get Matrix by ID
2. Save Matrix
3. Delete Matrix by ID
4. Add Matrices by IDs
5. Add Matrices from Body
6. Rotate Matrix by ID
7. Rotate Matrix from Body
8. Exit
