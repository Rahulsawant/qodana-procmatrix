package com.procmatrix.rotation.performance;

import com.procmatrix.rotation.service.MatrixRotationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class MatrixRotationPerformance {

    public static void main(String[] args) {
        // Create a large matrix for testing
        int rows = 10000;
        int cols = 10000;
        int[][] largeMatrix = generateLargeMatrix(rows, cols);

        // Create an instance of MatrixRotationService
        MatrixRotationService matrixRotationService = new MatrixRotationService(null, null, null);

        // Measure the performance of rotateMatrix function
        long startTime = System.nanoTime();
        int[][] rotatedMatrix = matrixRotationService.rotateMatrix(largeMatrix, 90);
        long endTime = System.nanoTime();

        // Calculate the elapsed time in milliseconds
        long elapsedTime = (endTime - startTime) / 1_000_000;
        System.out.println("Elapsed time: " + elapsedTime + " ms  " + elapsedTime/60 +" sec");
    }


    // Rotate a matrix by 90 degrees
    //1 2 3
    //4 5 6
    //7 8 9
    //matrix[0][0] (1) moves to matrix[0][2] (3)
    //matrix[0][2] (3) moves to matrix[2][2] (9)
    //matrix[2][2] (9) moves to matrix[2][0] (7)
    //matrix[2][0] (7) moves to matrix[0][0]
    // Then
    // matrix[0][1] (2) moves to matrix[1][2] (6)
    //matrix[1][2] (6) moves to matrix[2][1] (8)
    //matrix[2][1] (8) moves to matrix[1][0] (4)
    //matrix[1][0] (4) moves to matrix[0][1] (2)
    public int[][] rotateMatrixInPlace(int[][] matrix, int degree) {
        if (degree % 90 != 0) {
            throw new IllegalArgumentException("Degree must be a multiple of 90");
        }
        int n = matrix.length;
        int times = (degree / 90) % 4; // Number of 90-degree rotations

        for (int t = 0; t < times; t++) {
            IntStream.range(0, n / 2).parallel().forEach(i -> {
                for (int j = i; j < n - i - 1; j++) {
                    int temp = matrix[i][j];
                    matrix[i][j] = matrix[n - j - 1][i];
                    matrix[n - j - 1][i] = matrix[n - i - 1][n - j - 1];
                    matrix[n - i - 1][n - j - 1] = matrix[j][n - i - 1];
                    matrix[j][n - i - 1] = temp;
                }
            });
        }
        return matrix;
    }


    private static int[][] generateLargeMatrix(int rows, int cols) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(100);
            }
        }
        return matrix;
    }
}
