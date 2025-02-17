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

# Matrix Rotation API

## Overview
The **Matrix Rotation API** allows you to perform matrix rotation operations. This API supports rotating matrices by a specified degree, either by providing the matrix directly or by referencing a matrix by its ID.

## API Version
- Version: 1.0.0

## Endpoints

### Rotate Matrix by ID
- **Endpoint:** `/api/matrix/rotate/{id}`
- **Method:** `GET`
- **Description:** Rotate a matrix by its ID.
- **Parameters:**
    - `id` (path, required): ID of the matrix to rotate (integer, int64).
    - `degree` (query, required): Degree to rotate the matrix (must be a multiple of 90) (integer).
- **Responses:**
    - `200`: Matrix rotated successfully.
        - **Example Response:**
          ```json
          {
            "content": [
              [1, 2, 3],
              [4, 5, 6],
              [7, 8, 9]
            ],
            "message": "Matrix rotated successfully",
            "links": [
              {
                "rel": "rotate-90",
                "href": "http://localhost:8081/api/matrix/rotate/6?degree=90"
              },
              {
                "rel": "rotate-180",
                "href": "http://localhost:8081/api/matrix/rotate/6?degree=180"
              },
              {
                "rel": "rotate-360",
                "href": "http://localhost:8081/api/matrix/rotate/6?degree=360"
              }
            ]
          }
          ```
    - `400`: Invalid degree supplied.
    - `404`: Matrix not found.
    - `500`: Internal server error.

### Rotate Matrix
- **Endpoint:** `/api/matrix/rotate`
- **Method:** `POST`
- **Description:** Rotate a matrix provided in the request body.
- **Request Body:**
    - **Example Request:**
      ```json
      {
        "matrix": [
          [1, 2, 3],
          [4, 5, 6],
          [7, 8, 9]
        ],
        "degree": 90
      }
      ```
- **Responses:**
    - `200`: Matrix rotated successfully.
        - **Example Response:**
          ```json
          {
            "content": [
              [7, 4, 1],
              [8, 5, 2],
              [9, 6, 3]
            ],
            "message": "Matrix saved & rotated successfully",
            "links": [
              {
                "rel": "rotate-90",
                "href": "http://localhost:8081/api/matrix/rotate/6?degree=90"
              },
              {
                "rel": "rotate-180",
                "href": "http://localhost:8081/api/matrix/rotate/6?degree=180"
              },
              {
                "rel": "rotate-360",
                "href": "http://localhost:8081/api/matrix/rotate/6?degree=360"
              }
            ]
          }
          ```
    - `400`: Invalid matrix data supplied.
    - `500`: Internal server error.

Note: above responses are auto generated samples for reference

### Schemas

#### MatrixResponse
- **Type:** `object`
- **Properties:**
    - `content` (array of arrays of integers): The rotated matrix.
    - `message` (string): Response message.
    - `links` (array of `Link` objects): Related links.

#### RotateMatrixRequest
- **Type:** `object`
- **Properties:**
    - `matrix` (array of arrays of integers): The matrix to rotate.
    - `degree` (integer): Degree to rotate the matrix (must be a multiple of 90).

#### MatrixRequest
- **Type:** `object`
- **Properties:**
    - `matrix` (array of arrays of integers): The matrix to rotate.

#### Link
- **Type:** `object`
- **Properties:**
    - `rel` (string): The relation type of the link.
    - `href` (string): The URL of the link.

## Tags
- **Matrix Rotation API:** API for matrix rotation operations.
