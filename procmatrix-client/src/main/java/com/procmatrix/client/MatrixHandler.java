package com.procmatrix.client;

import com.procmatrix.model.MatrixResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class MatrixHandler {

    private final com.procmatrix.client.service.MatrixClientService matrixService;

    @Autowired
    public MatrixHandler(com.procmatrix.client.service.MatrixClientService matrixService) {
        this.matrixService = matrixService;
    }

    void handleGetMatrix(Scanner scanner) {
        try {
            System.out.print("Enter Matrix ID: ");
            Long getId = scanner.nextLong();
            MatrixResponse getMatrix = matrixService.getMatrix(getId);
            System.out.println("Matrix: " + getMatrix);
        } catch (Exception e) {
            System.err.println("Error getting matrix: " + e.getMessage());
        }
    }

    void handleSaveMatrix(Scanner scanner) {
        try {
            System.out.println("Enter Matrix data (comma-separated rows, semicolon-separated columns) e.g. 1,2,3;4,5,6 for [[1, 2, 3], [4, 5, 6]] :");
            String matrixData = scanner.nextLine();
            int[][] newMatrix = parseMatrix(matrixData);
            MatrixResponse savedMatrix = matrixService.saveMatrix(newMatrix);
            System.out.println("Saved Matrix: " + savedMatrix);
        } catch (Exception e) {
            System.err.println("Error saving matrix: " + e.getMessage());
        }
    }

    void handleDeleteMatrix(Scanner scanner) {
        try {
            System.out.print("Enter Matrix ID to delete: ");
            Long deleteId = scanner.nextLong();
            matrixService.deleteMatrix(deleteId);
            System.out.println("Matrix deleted");
        } catch (Exception e) {
            System.err.println("Error deleting matrix: " + e.getMessage());
        }
    }

    void handleAddMatrices(Scanner scanner) {
        try {
            System.out.print("Enter first Matrix ID: ");
            Long matrix1Id = scanner.nextLong();
            System.out.print("Enter second Matrix ID: ");
            Long matrix2Id = scanner.nextLong();
            MatrixResponse addedMatrix = matrixService.addMatrices(matrix1Id, matrix2Id);
            System.out.println("Added Matrix: " + addedMatrix);
        } catch (Exception e) {
            System.err.println("Error adding matrices: " + e.getMessage());
        }
    }

    void handleAddMatricesFromBody(Scanner scanner) {
        try {
            System.out.println("Enter first Matrix data (comma-separated rows, semicolon-separated columns) e.g. 1,2,3;4,5,6 for [[1, 2, 3], [4, 5, 6]] :");
            String matrix1Data = scanner.nextLine();
            System.out.println("Enter second Matrix data (comma-separated rows, semicolon-separated columns) e.g. 1,2,3;4,5,6 for [[1, 2, 3], [4, 5, 6]] :");
            String matrix2Data = scanner.nextLine();
            int[][] matrix1 = parseMatrix(matrix1Data);
            int[][] matrix2 = parseMatrix(matrix2Data);
            MatrixResponse addedMatrixFromBody = matrixService.addMatricesFromBody(matrix1, matrix2);
            System.out.println("Added Matrix from Body: " + addedMatrixFromBody);
        } catch (Exception e) {
            System.err.println("Error adding matrices from body: " + e.getMessage());
        }
    }

    void handleRotateMatrixById(Scanner scanner) {
        try {
            System.out.print("Enter Matrix ID to rotate: ");
            Long rotateId = scanner.nextLong();
            System.out.print("Enter degree to rotate: ");
            int degree = scanner.nextInt();
            com.procmatrix.rotation.model.MatrixResponse rotatedMatrixById = matrixService.rotateMatrixById(rotateId, degree);
            System.out.println("Rotated Matrix by ID: " + rotatedMatrixById);
        } catch (Exception e) {
            System.err.println("Error rotating matrix by ID: " + e.getMessage());
        }
    }

    void handleRotateMatrixFromBody(Scanner scanner) {
        try {
            System.out.println("Enter Matrix data to rotate (comma-separated rows, semicolon-separated columns) e.g. 1,2,3;4,5,6 for [[1, 2, 3], [4, 5, 6]] : ");
            String matrixToRotateData = scanner.nextLine();
            int[][] matrixToRotate = parseMatrix(matrixToRotateData);
            System.out.print("Enter degree to rotate: ");
            int rotateDegree = scanner.nextInt();
            com.procmatrix.rotation.model.MatrixResponse rotatedMatrixFromBody = matrixService.rotateMatrixFromBody(matrixToRotate, rotateDegree);
            System.out.println("Rotated Matrix from Body: " + rotatedMatrixFromBody);
        } catch (Exception e) {
            System.err.println("Error rotating matrix from body: " + e.getMessage());
        }
    }

    private int[][] parseMatrix(String matrixData) {
        try {
            String[] rows = matrixData.split(";");
            int[][] matrix = new int[rows.length][];
            for (int i = 0; i < rows.length; i++) {
                String[] columns = rows[i].split(",");
                matrix[i] = new int[columns.length];
                for (int j = 0; j < columns.length; j++) {
                    matrix[i][j] = Integer.parseInt(columns[j]);
                }
            }
            return matrix;
        } catch (Exception e) {
            System.err.println("Error parsing matrix data: " + e.getMessage());
            return new int[0][0]; // Return an empty matrix in case of error
        }
    }
}
