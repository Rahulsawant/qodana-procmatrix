## API Details

### Procmatrix Spring Boot Application

The `MatrixController` handles the following APIs:

1. **Retrieving a Matrix**:
  - **Method**: `getMatrix`
  - **Endpoint**: `GET /api/matrix/{id}`
  - **Roles**: `CREATE`, `READ`, `OPERATIONS`
  - **Description**: Validates the matrix ID, retrieves the matrix using `matrixService`, and returns the matrix data. If not found, returns `404 NOT FOUND`.

2. **Saving a Matrix**:
  - **Method**: `saveMatrix`
  - **Endpoint**: `POST /api/matrix`
  - **Role**: `CREATE`
  - **Description**: Validates the matrix data, saves it using `matrixService`, and returns the result. If validation fails, returns `400 BAD REQUEST`. If save fails, returns `500 INTERNAL SERVER ERROR`.

3. **Deleting a Matrix**:
  - **Method**: `deleteMatrix`
  - **Endpoint**: `DELETE /api/matrix/{id}`
  - **Role**: `CREATE`
  - **Description**: Validates the matrix ID, deletes the matrix using `matrixService`, and returns the status. If successful, returns `204 NO CONTENT`. If not found, returns `404 NOT FOUND`.

The `MatrixAdditionController` handles the following APIs:

1. **Adding Matrices by IDs**:
  - **Method**: `addMatrices`
  - **Endpoint**: `GET /api/matrix/add/{id1}/{id2}`
  - **Roles**: `CREATE`, `OPERATIONS`
  - **Description**: Validates the matrix IDs, retrieves the matrices using `matrixService`, adds them, and returns the result wrapped in a `MatrixResponse` object. If any matrix is not found, returns a `404 NOT FOUND` status.

2. **Adding Matrices from Request Body**:
  - **Method**: `addMatrices`
  - **Endpoint**: `POST /api/matrix/add`
  - **Roles**: `CREATE`, `OPERATIONS`
  - **Description**: Validates the matrices provided in the request body, adds them using `matrixService`, and returns the result wrapped in a `MatrixResponse` object. If validation fails, returns a `400 BAD REQUEST` status.
