Project contains mainly below repositories
1. procmatrix-core - it contains library classes
2. procmatrix - it contains spring boot application to provide matrix functionalities
3. procmatrix-rotation - it contains matrix rotation functionality
4. procmatrix-client- it contains client utility class to access apis


Procmatrix Spring boot application handles below apis
The MatrixController handle below three main apis:  
- Retrieving a Matrix:  
  Method: getMatrix
  Endpoint: GET /api/matrix/{id}
  Roles: CREATE, READ, OPERATIONS
  Validates the matrix ID, retrieves the matrix using matrixService, and returns the matrix data. If not found, returns 404 NOT FOUND.
- Saving a Matrix:  
  Method: saveMatrix
  Endpoint: POST /api/matrix
  Role: CREATE
  Validates the matrix data, saves it using matrixService, and returns the result. If validation fails, returns 400 BAD REQUEST. If save fails, returns 500 INTERNAL SERVER ERROR.
- Deleting a Matrix:  
  Method: deleteMatrix
  Endpoint: DELETE /api/matrix/{id}
  Role: CREATE
  Validates the matrix ID, deletes the matrix using matrixService, and returns the status. If successful, returns 204 NO CONTENT. If not found, returns 404 NOT FOUND.

The MatrixAdditionController handles below two apis:  
- Adding Matrices by IDs:  
  Method: addMatrices
  Endpoint: GET /api/matrix/add/{id1}/{id2}
  Roles: CREATE, OPERATIONS
  Validates the matrix IDs, retrieves the matrices using matrixService, adds them, and returns the result wrapped in a MatrixResponse object. If any matrix is not found, returns a 404 NOT FOUND status.
- Adding Matrices from Request Body:  
  Method: addMatrices
  Endpoint: POST /api/matrix/add
  Roles: CREATE, OPERATIONS
  Validates the matrices provided in the request body, adds them using matrixService, and returns the result wrapped in a MatrixResponse object. If validation fails, returns a 400 BAD REQUEST status.

procmatrix-rotation Spring boot application handles below apis

The MatrixRotationController includes two main methods:  
- Rotating a Matrix by ID:  
  Method: rotateMatrix
  Endpoint: GET /api/matrix/rotate/{id}
  Roles: CREATE, OPERATIONS
  Validates the degree parameter, retrieves the matrix by ID using matrixRotationService, rotates it, and returns the rotated matrix wrapped in a MatrixResponse object. If the matrix is not found, returns a 404 NOT FOUND status.
- Rotating a Matrix from Request Body:  
  Method: rotateMatrix
  Endpoint: POST /api/matrix/rotate
  Roles: CREATE, OPERATIONS
  Validates the matrix and degree provided in the request body, rotates the matrix using matrixRotationService, and returns the rotated matrix wrapped in a MatrixResponse object. If validation fails, returns a 400 BAD REQUEST status.
