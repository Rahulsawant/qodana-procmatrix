package com.procmatrix.rotation.controller;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.core.utils.InputValidator;
import com.procmatrix.rotation.entity.RotateMatrixRequest;
import com.procmatrix.rotation.service.MatrixRotationService;
import com.procmatrix.rotation.utils.MatrixResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matrix")
public class MatrixRotationController {

    private static final Logger logger = LoggerFactory.getLogger(MatrixRotationController.class);

    private final MatrixRotationService matrixRotationService;

    @Autowired
    public MatrixRotationController(MatrixRotationService matrixRotationService) {
        this.matrixRotationService = matrixRotationService;
    }

    @GetMapping("/rotate/{id}")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> rotateMatrix(@PathVariable(name = "id") Long id, @RequestParam(name = "degree", defaultValue = "90") int degree) {
        logger.debug("Received request to rotate matrix with ID {} by {} degrees", id, degree);
        try {
            validateDegree(degree);
            int[][] matrix = matrixRotationService.getMatrix(id);
            if (matrix == null) {
                return MatrixResponseBuilder.buildNotFoundResponse(id);
            }
            int[][] rotatedMatrix = matrixRotationService.rotateMatrix(matrix, degree);
            return buildResponse(rotatedMatrix, id, MatrixResponseBuilder.ResponseMessages.MATRIX_ROTATED_SUCCESSFULLY);
        } catch (IllegalArgumentException e) {
            return MatrixResponseBuilder.buildBadRequestResponse(id, e);
        } catch (Exception e) {
            return MatrixResponseBuilder.buildErrorResponse(id, e);
        }
    }

    @PostMapping("/rotate")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> rotateMatrix(@RequestBody RotateMatrixRequest matrixRequest) {
        logger.debug("Received request to rotate matrix");
        try {
            InputValidator.validateMatrixRequest(matrixRequest);
            int[][] rotatedMatrix = matrixRotationService.rotateMatrix(matrixRequest.getMatrix(), matrixRequest.getDegree());
            MatrixData matrixData = matrixRotationService.saveMatrix(matrixRequest);
            if (matrixData == null) {
                logger.error("Failed to save matrix, we'll just return rotated matrix to user");
                return buildResponse(rotatedMatrix, null, MatrixResponseBuilder.ResponseMessages.MATRIX_ROTATED_SUCCESSFULLY);
            }
            return buildResponse(rotatedMatrix, matrixData.getId(), MatrixResponseBuilder.ResponseMessages.MATRIX_STORED_AND_ROTATED);
        } catch (IllegalArgumentException e) {
            return MatrixResponseBuilder.buildBadRequestResponse(e);
        } catch (Exception e) {
            return MatrixResponseBuilder.buildErrorResponse(e);
        }
    }

    private void validateDegree(int degree) {
        if (degree % 90 != 0) {
            throw new IllegalArgumentException(MatrixResponseBuilder.ResponseMessages.MATRIX_DEGREE_MULTIPLE_OF_90);
        }
    }

    private ResponseEntity<MatrixResponse> buildResponse(int[][] matrix, Long id, String successMessage) {
        MatrixResponse response = MatrixResponseBuilder.buildMatrixResponse(matrix, id, successMessage);
        logger.debug("Successfully processed matrix with ID {}", id);
        return ResponseEntity.ok(response);
    }
}