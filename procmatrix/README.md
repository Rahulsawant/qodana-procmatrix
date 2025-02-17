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


# Matrix API

## Overview
The Matrix API allows you to perform various operations on matrices, such as retrieving, saving, deleting, and adding matrices.

## API Endpoints
please see below samples of apis, and the details of the api
### Get Matrix by ID
- **Endpoint:** `GET /api/matrix/{id}`
- **Description:** Retrieve a matrix by its ID.
- **Parameters:**
    - `id` (path, integer, required): ID of the matrix to retrieve.
- **Responses:**
    - `200`: Matrix retrieved successfully.
      ```json
      {
        "id": 1,
        "data": [[1, 2], [3, 4]],
        "message": "Matrix retrieved successfully",
        "links": [
          {
            "rel": "self",
            "href": "/api/matrix/1"
          }
        ]
      }
      ```
    - `400`: Invalid ID supplied.
      ```json
      {
        "error": "Invalid ID supplied"
      }
      ```
    - `404`: Matrix not found.
      ```json
      {
        "error": "Matrix not found"
      }
      ```
    - `500`: Internal server error.
      ```json
      {
        "error": "Internal server error"
      }
      ```

### Delete Matrix by ID
- **Endpoint:** `DELETE /api/matrix/{id}`
- **Description:** Delete a matrix by its ID.
- **Parameters:**
    - `id` (path, integer, required): ID of the matrix to delete.
- **Responses:**
    - `204`: Matrix deleted successfully.
    - `400`: Invalid ID supplied.
      ```json
      {
        "error": "Invalid ID supplied"
      }
      ```
    - `404`: Matrix not found.
      ```json
      {
        "error": "Matrix not found"
      }
      ```
    - `500`: Internal server error.
      ```json
      {
        "error": "Internal server error"
      }
      ```

### Save Matrix
- **Endpoint:** `POST /api/matrix`
- **Description:** Save a new matrix.
- **Request Body:**
    - `MatrixRequest` (application/json): The matrix data to save.
      ```json
      {
        "data": [[1, 2], [3, 4]]
      }
      ```
- **Responses:**
    - `200`: Matrix saved successfully.
      ```json
      {
        "id": 1,
        "data": [[1, 2], [3, 4]],
        "message": "Matrix saved successfully",
        "links": [
          {
            "rel": "self",
            "href": "/api/matrix/1"
          }
        ]
      }
      ```
    - `400`: Invalid matrix data supplied.
      ```json
      {
        "error": "Invalid matrix data supplied"
      }
      ```
    - `500`: Internal server error.
      ```json
      {
        "error": "Internal server error"
      }
      ```

### Add Matrices by IDs
- **Endpoint:** `GET /api/matrix/add/{id1}/{id2}`
- **Description:** Adds two matrices by their IDs.
- **Parameters:**
    - `id1` (path, integer, required): ID of the first matrix.
    - `id2` (path, integer, required): ID of the second matrix.
- **Responses:**
    - `200`: Matrices added successfully.
      ```json
      {
        "data": [[2, 4], [6, 8]],
        "message": "Matrices added successfully",
        "links": [
          {
            "rel": "self",
            "href": "/api/matrix/1"
          },
          {
            "rel": "self",
            "href": "/api/matrix/2"
          }
        ]
      }
      ```
    - `400`: Invalid IDs supplied.
      ```json
      {
        "error": "Invalid IDs supplied"
      }
      ```
    - `404`: Matrix not found.
      ```json
      {
        "error": "Matrix not found"
      }
      ```
    - `500`: Internal server error.
      ```json
      {
        "error": "Internal server error"
      }
      ```

### Add Matrices
- **Endpoint:** `POST /api/matrix/add`
- **Description:** Adds two matrices provided in the request body.
- **Request Body:**
    - `MatrixAdditionRequest` (application/json): The matrices to add.
      ```json
      {
        "matrix1": [[1, 2], [3, 4]],
        "matrix2": [[1, 2], [3, 4]]
      }
      ```
- **Responses:**
    - `200`: Matrices added successfully.
      ```json
      {
        "data": [[2, 4], [6, 8]],
        "message": "Matrices added successfully"
      }
      ```
    - `400`: Invalid matrix data supplied.
      ```json
      {
        "error": "Invalid matrix data supplied"
      }
      ```
    - `500`: Internal server error.
      ```json
      {
        "error": "Internal server error"
      }
      ```

## Components
Note- above are example response generated with swagger yaml

### Schemas

#### MatrixRequest
- **Type:** object
- **Properties:**
    - `data` (array of arrays of integers): The matrix data.

#### MatrixResponse
- **Type:** object
- **Properties:**
    - `id` (integer): The ID of the matrix.
    - `data` (array of arrays of integers): The matrix data.
    - `message` (string): A message describing the result.
    - `links` (array of objects): Related links.
        - `rel` (string): The relationship of the link.
        - `href` (string): The URL of the link.

#### MatrixAdditionRequest
- **Type:** object
- **Properties:**
    - `matrix1` (array of arrays of integers): The first matrix.
    - `matrix2` (array of arrays of integers): The second matrix.

#### Link
- **Type:** object
- **Properties:**
    - `rel` (string): The relationship of the link.
    - `href` (string): The URL of the link.


