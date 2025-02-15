package com.qodana.procmatrix.controller;

import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.core.utils.InputValidator;
import com.qodana.procmatrix.entity.MatrixAdditionRequest;
import com.qodana.procmatrix.service.MatrixService;
import com.qodana.procmatrix.utils.MatrixResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.qodana.procmatrix.utils.MatrixResponseBuilder.ResponseMessages.*;

@RestController
@RequestMapping("/api/matrix")
public class MatrixAdditionController {
    @Autowired
    private MatrixService matrixService;

    @GetMapping("/add/{id1}/{id2}")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> addMatrices(@PathVariable Long id1, @PathVariable Long id2) {
        InputValidator.validateIds(id1, id2);
        int[][] matrix1 = matrixService.getMatrix(id1);
        int[][] matrix2 = matrixService.getMatrix(id2);

        if (matrix1 == null || matrix2 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MatrixResponseBuilder.buildMatrixAddResponse(null, id1, id2, MATRIX_NOT_FOUND));
        }
        //TODO matrix addition & result in Db again? may be keep reference of it
        int[][] result = addMatrices(matrix1, matrix2);
        MatrixResponse response = MatrixResponseBuilder.buildMatrixAddResponse(result, id1, id2, MATRIX_ADDED_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> addMatrices(@RequestBody MatrixAdditionRequest request) {
        try {
            InputValidator.validateMatrixRequests(request.getMatrix1(),request.getMatrix2());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MatrixResponse(null, null, e.getMessage()));
        }

        int[][] result = addMatrices(request.getMatrix1().getMatrix(), request.getMatrix2().getMatrix());
        MatrixResponse response = MatrixResponseBuilder.buildMatrixResponse(result, null, MATRIX_ADDED_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    private int[][] addMatrices(int[][] matrix1, int[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }
}