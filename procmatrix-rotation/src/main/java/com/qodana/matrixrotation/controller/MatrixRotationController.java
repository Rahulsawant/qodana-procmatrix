package com.qodana.matrixrotation.controller;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.core.utils.InputValidator;
import com.qodana.matrixrotation.entity.RotateMatrixRequest;
import com.qodana.matrixrotation.service.MatrixRotationService;
import com.qodana.matrixrotation.utils.MatrixResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.qodana.matrixrotation.utils.MatrixResponseBuilder.ResponseMessages.*;

@RestController
@RequestMapping("/api/matrix")
public class MatrixRotationController {

    private static final Logger logger = LoggerFactory.getLogger(MatrixRotationController.class);


    private MatrixRotationService matrixRotationService;

    @Autowired
    public MatrixRotationController(MatrixRotationService matrixRotationService) {
        this.matrixRotationService = matrixRotationService;
    }

    @GetMapping("/rotate/{id}")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> rotateMatrix(@PathVariable(name = "id") Long id, @RequestParam(name = "degree", defaultValue = "90") int degree) {
        logger.debug("Received request to rotate matrix with ID {} by {} degrees", id, degree);
        try {
            if (degree % 90 != 0) {
                return MatrixResponseBuilder.buildBadRequestResponse(id, new IllegalArgumentException(MATRIX_DEGREE_MULTIPLE_OF_90));
            }
            int[][] matrix = matrixRotationService.getMatrix(id);
            if (matrix == null) {
                return MatrixResponseBuilder.buildNotFoundResponse(id);
            }
            int[][] rotatedMatrix = matrixRotationService.rotateMatrix(matrix, degree);
            MatrixResponse response = MatrixResponseBuilder.buildMatrixResponse(rotatedMatrix, id, MATRIX_ROTATED_SUCCESSFULLY);
            logger.debug("Successfully rotated matrix with ID {}", id);
            return ResponseEntity.ok(response);
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
            MatrixData matrixData=matrixRotationService.saveMatrix(matrixRequest);
            if(matrixData == null){
                logger.error("Failed to save matrix, we'll just return rotated matrix to user");
                return ResponseEntity.ok(MatrixResponseBuilder.buildMatrixResponse(rotatedMatrix, null, MATRIX_ROTATED_SUCCESSFULLY));
            }
            logger.debug("Successfully stored and rotated matrix with ID {}", matrixData.getId());
            return ResponseEntity.ok(MatrixResponseBuilder.buildMatrixResponse(rotatedMatrix, matrixData.getId(), MATRIX_STORED_AND_ROTATED));
        } catch (IllegalArgumentException e) {
            return MatrixResponseBuilder.buildBadRequestResponse(e);
        } catch (Exception e) {
            return MatrixResponseBuilder.buildErrorResponse(e);
        }
    }
}