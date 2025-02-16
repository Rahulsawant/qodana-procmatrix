package com.qodana.procmatrix.utils;

import com.procmatrix.core.entity.MatrixResponse;
import com.qodana.procmatrix.controller.MatrixAdditionController;
import com.qodana.procmatrix.controller.MatrixController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class MatrixResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MatrixResponseBuilder.class);

    public static class ResponseMessages {
        public static final String MATRIX_NOT_FOUND = "Matrix not found.";
        public static final String MATRIX_RETRIEVED_SUCCESSFULLY = "Matrix retrieved successfully.";
        public static final String MATRIX_SAVED_SUCCESSFULLY = "Matrix saved successfully.";
        public static final String FAILED_TO_SAVE_MATRIX = "Failed to save matrix.";
        public static final String MATRIX_ADDED_SUCCESSFULLY = "Matrices added successfully.";
    }

    public static MatrixResponse buildMatrixResponse(int[][] matrix, Long id, String message) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixController.class).getMatrix(id)).withSelfRel());
        return new MatrixResponse(matrix, links, message);
    }

    public static MatrixResponse buildMatrixAddResponse(int[][] matrix, Long id1, Long id2, String message) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixAdditionController.class).addMatrices(id1, id2)).withSelfRel());
        return new MatrixResponse(matrix, links, message);
    }

    public static ResponseEntity<MatrixResponse> buildNotFoundResponse(Long id) {
        logger.warn("Matrix with ID {} not found", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildMatrixResponse(null, id, ResponseMessages.MATRIX_NOT_FOUND));
    }

    public static ResponseEntity<MatrixResponse> buildBadRequestResponse(Long id, IllegalArgumentException e) {
        logger.warn("Invalid ID {}: {}", id, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MatrixResponse(null, null, e.getMessage()));
    }

    public static ResponseEntity<MatrixResponse> buildBadRequestResponse(IllegalArgumentException e) {
        logger.warn("Invalid request: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MatrixResponse(null, null, e.getMessage()));
    }

    public static ResponseEntity<MatrixResponse> buildErrorResponse(Long id, Exception e) {
        logger.error("Error processing request for matrix with ID {}", id, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MatrixResponse(null, null, "Error processing request"));
    }

    public static ResponseEntity<MatrixResponse> buildErrorResponse(Exception e) {
        logger.error("Error processing request", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MatrixResponse(null, null, "Error processing request"));
    }

    public static ResponseEntity<MatrixResponse> buildInternalServerErrorResponse() {
        logger.warn("Failed to save matrix");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MatrixResponse(null, null, "Failed to save matrix"));
    }
}