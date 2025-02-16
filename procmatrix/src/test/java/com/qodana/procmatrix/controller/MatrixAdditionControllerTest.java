package com.qodana.procmatrix.controller;

import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.core.utils.InputValidator;
import com.qodana.procmatrix.entity.MatrixAdditionRequest;
import com.qodana.procmatrix.service.MatrixService;
import com.qodana.procmatrix.utils.MatrixResponseBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.qodana.procmatrix.utils.MatrixResponseBuilder.ResponseMessages.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class MatrixAdditionControllerTest {

    @Mock
    private MatrixService matrixService;

    @InjectMocks
    private MatrixAdditionController matrixAdditionController;

    @Test
    @WithMockUser(roles = {"CREATE", "OPERATIONS"})
    void addMatrices_ValidRequest_ReturnsMatrixResponse() {
        MatrixAdditionRequest request = new MatrixAdditionRequest();
        request.setMatrix1(buildMatrixRequest(new int[][]{{1, 2}, {3, 4}}));
        request.setMatrix2(buildMatrixRequest(new int[][]{{5, 6}, {7, 8}}));
        int[][] expectedResult = {{6, 8}, {10, 12}};

        ResponseEntity<MatrixResponse> response = matrixAdditionController.addMatrices(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(expectedResult, response.getBody().getContent());
        assertEquals(MATRIX_ADDED_SUCCESSFULLY, response.getBody().getMessage());
    }

    @Test
    @WithMockUser(roles = {"CREATE", "OPERATIONS"})
    void addMatrices_InvalidRequest_ReturnsBadRequest() {
        MatrixAdditionRequest request = new MatrixAdditionRequest();
        request.setMatrix1(buildMatrixRequest(new int[][]{{1, 2}, {3}}));
        request.setMatrix2(buildMatrixRequest(new int[][]{{5, 6}, {7, 8}}));

        ResponseEntity<MatrixResponse> response = matrixAdditionController.addMatrices(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @WithMockUser(roles = {"CREATE", "OPERATIONS"})
    void addMatrices_EmptyMatrices_ReturnsBadRequest() {
        MatrixAdditionRequest request = new MatrixAdditionRequest();
        request.setMatrix1(buildMatrixRequest(new int[][]{}));
        request.setMatrix2(buildMatrixRequest(new int[][]{}));

        ResponseEntity<MatrixResponse> response = matrixAdditionController.addMatrices(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @WithMockUser(roles = {"CREATE", "OPERATIONS"})
    void addMatrices_NullMatrices_ReturnsBadRequest() {
        MatrixAdditionRequest request = new MatrixAdditionRequest();
        request.setMatrix1(buildMatrixRequest(null));
        request.setMatrix2(buildMatrixRequest(null));

        ResponseEntity<MatrixResponse> response = matrixAdditionController.addMatrices(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private MatrixRequest buildMatrixRequest(int[][] matrix) {
        MatrixRequest matrixRequest = new MatrixRequest();
        matrixRequest.setMatrix(matrix);
        return matrixRequest;
    }

}