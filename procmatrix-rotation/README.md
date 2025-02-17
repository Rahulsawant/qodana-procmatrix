### procmatrix-rotation Spring Boot Application

The `MatrixRotationController` includes two main methods:

1. **Rotating a Matrix by ID**:
  - **Method**: `rotateMatrix`
  - **Endpoint**: `GET /api/matrix/rotate/{id}`
  - **Roles**: `CREATE`, `OPERATIONS`
  - **Description**: Validates the degree parameter, retrieves the matrix by ID using `matrixRotationService`, rotates it, and returns the rotated matrix wrapped in a `MatrixResponse` object. If the matrix is not found, returns `404 NOT FOUND`.

2. **Rotating a Matrix from Request Body**:
  - **Method**: `rotateMatrix`
  - **Endpoint**: `POST /api/matrix/rotate`
  - **Roles**: `CREATE`, `OPERATIONS`
  - **Description**: Validates the matrix and degree provided in the request body, rotates the matrix using `matrixRotationService`, and returns the rotated matrix wrapped in a `MatrixResponse` object. If validation fails, returns `400 BAD REQUEST`.
