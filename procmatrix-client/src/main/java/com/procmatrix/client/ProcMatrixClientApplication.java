package com.procmatrix.client;

import com.procmatrix.api.MatrixApiApi;
import com.procmatrix.invoker.ApiException;
import com.procmatrix.model.MatrixAdditionRequest;
import com.procmatrix.model.MatrixRequest;
import com.procmatrix.model.MatrixResponse;
import com.procmatrix.rotation.api.MatrixRotationApiApi;
import com.procmatrix.rotation.model.RotateMatrixRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ProcMatrixClientApplication implements CommandLineRunner {

    private final MatrixApiApi matrixApi;
    private final MatrixRotationApiApi matrixRotationApi;

    @Autowired
    public ProcMatrixClientApplication(MatrixApiApi matrixApi, MatrixRotationApiApi matrixRotationApi){
        this.matrixApi = matrixApi;
        this.matrixRotationApi = matrixRotationApi;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProcMatrixClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            // Get a matrix by ID
            MatrixResponse matrix = getMatrix(1L);
            System.out.println("Matrix: " + matrix);

            // Save a new matrix
            int[][] newMatrix = {{1, 2}, {3, 4}};
            MatrixResponse savedMatrix = saveMatrix(newMatrix);
            System.out.println("Saved Matrix: " + savedMatrix);

            // Delete a matrix by ID
            deleteMatrix(1L);
            System.out.println("Matrix deleted");

            // Add two matrices by IDs
            MatrixResponse addedMatrix = addMatrices(1L, 2L);
            System.out.println("Added Matrix: " + addedMatrix);

            // Add two matrices from body
            int[][] matrix1 = {{1, 2}, {3, 4}};
            int[][] matrix2 = {{5, 6}, {7, 8}};
            MatrixResponse addedMatrixFromBody = addMatricesFromBody(matrix1, matrix2);
            System.out.println("Added Matrix from Body: " + addedMatrixFromBody);

            // Rotate a matrix by ID

            com.procmatrix.rotation.model.MatrixResponse rotatedMatrixById = rotateMatrixById(1L, 90);
            System.out.println("Rotated Matrix by ID: " + rotatedMatrixById);

            // Rotate a matrix from body
            int[][] matrixToRotate = {{1, 2}, {3, 4}};

            com.procmatrix.rotation.model.MatrixResponse rotatedMatrixFromBody = rotateMatrixFromBody(matrixToRotate, 90);
            System.out.println("Rotated Matrix from Body: " + rotatedMatrixFromBody);

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public MatrixResponse getMatrix(Long matrixId) throws ApiException {
        return matrixApi.getMatrix(matrixId);
    }

    public MatrixResponse saveMatrix(int[][] matrixData) throws ApiException {
        MatrixRequest request = new MatrixRequest();
        request.setMatrix(convertArrayToList(matrixData));
        return matrixApi.saveMatrix(request);
    }

    public void deleteMatrix(Long matrixId) throws ApiException {
        matrixApi.deleteMatrix(matrixId);
    }

    public MatrixResponse addMatrices(Long matrix1Id, Long matrix2Id) throws ApiException {
        return matrixApi.addMatricesByIds(matrix1Id, matrix2Id);
    }

    public MatrixResponse addMatricesFromBody(int[][] matrix1, int[][] matrix2) throws ApiException {
        MatrixAdditionRequest request = new MatrixAdditionRequest();
        MatrixRequest matrixRequest1 = new MatrixRequest();
        matrixRequest1.setMatrix(convertArrayToList(matrix1));
        request.setMatrix1(matrixRequest1);

        MatrixRequest matrixRequest2 = new MatrixRequest();
        matrixRequest2.setMatrix(convertArrayToList(matrix2));
        request.setMatrix2(matrixRequest2);

        return matrixApi.addMatrices(request);
    }

    public com.procmatrix.rotation.model.MatrixResponse rotateMatrixById(Long matrixId, int degree) throws ApiException {
        try {
            return matrixRotationApi.rotateMatrixById(matrixId, degree);
        } catch (com.procmatrix.rotation.invoker.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public com.procmatrix.rotation.model.MatrixResponse rotateMatrixFromBody(int[][] matrixData, int degree) throws ApiException {
        RotateMatrixRequest request = new RotateMatrixRequest();

        com.procmatrix.rotation.model.MatrixRequest matrixRequest = new com.procmatrix.rotation.model.MatrixRequest();
        matrixRequest.setMatrix(convertArrayToList(matrixData));
        request.setMatrix(matrixRequest);
        request.setDegree(degree);
        try {
            return matrixRotationApi.rotateMatrix(request);
        } catch (com.procmatrix.rotation.invoker.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private List<List<Integer>> convertArrayToList(int[][] array) {
        List<List<Integer>> list = new ArrayList<>();
        for (int[] row : array) {
            List<Integer> rowList = new ArrayList<>();
            for (int value : row) {
                rowList.add(value);
            }
            list.add(rowList);
        }
        return list;
    }
}