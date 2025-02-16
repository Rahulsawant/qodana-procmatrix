package com.procmatrix.controller;

import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.core.utils.InputValidator;
import com.procmatrix.entity.MatrixAdditionRequest;
import com.procmatrix.service.MatrixService;
import com.procmatrix.utils.MatrixResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.procmatrix.utils.MatrixResponseBuilder.ResponseMessages.*;

/**
 * REST controller for handling matrix addition operations.
 */
@RestController
@RequestMapping("/api/matrix")
public class MatrixAdditionController {

    private static final Logger logger = LoggerFactory.getLogger(MatrixAdditionController.class);

    private final MatrixService matrixService;

    @Autowired
    public MatrixAdditionController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    /**
     * Adds two matrices by their IDs.
     *
     * @param id1 the ID of the first matrix
     * @param id2 the ID of the second matrix
     * @return ResponseEntity containing the result of the addition and status
     */
    @GetMapping("/add/{id1}/{id2}")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> addMatrices(@PathVariable Long id1, @PathVariable Long id2) {
        logger.debug("Received request to add matrices with IDs {} and {}", id1, id2);
        try {
            InputValidator.validateIds(id1, id2);
            int[][] matrix1 = matrixService.getMatrix(id1);
            int[][] matrix2 = matrixService.getMatrix(id2);

            if (matrix1 == null || matrix2 == null) {
                return MatrixResponseBuilder.buildNotFoundResponse(id1);
            }

            int[][] result = matrixService.addMatrices(matrix1, matrix2);
            MatrixResponse response = MatrixResponseBuilder.buildMatrixAddResponse(result, id1, id2, MATRIX_ADDED_SUCCESSFULLY);
            logger.debug("Successfully added matrices with IDs {} and {}", id1, id2);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return MatrixResponseBuilder.buildBadRequestResponse(e);
        } catch (Exception e) {
            return MatrixResponseBuilder.buildErrorResponse(e);
        }
    }

    /**
     * Adds two matrices provided in the request body.
     *
     * @param request the request containing the matrices to add
     * @return ResponseEntity containing the result of the addition and status
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> addMatrices(@RequestBody MatrixAdditionRequest request) {
        logger.debug("Received request to add matrices");
        try {
            InputValidator.validateMatrixRequests(request.getMatrix1(), request.getMatrix2());
            int[][] result = matrixService.addMatrices(request.getMatrix1().getMatrix(), request.getMatrix2().getMatrix());
            MatrixResponse response = MatrixResponseBuilder.buildMatrixAddResponse(result, null, null, MATRIX_ADDED_SUCCESSFULLY);
            logger.debug("Successfully added matrices");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return MatrixResponseBuilder.buildBadRequestResponse(e);
        } catch (Exception e) {
            return MatrixResponseBuilder.buildErrorResponse(e);
        }
    }
}