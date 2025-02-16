package com.procmatrix.rotation.utils;

import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.rotation.controller.MatrixRotationController;
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

    public static MatrixResponse buildMatrixResponse(int[][] matrix, Long id, String message) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id, 90)).withRel("rotate-90"));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id, 180)).withRel("rotate-180"));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id, 360)).withRel("rotate-360"));
        return new MatrixResponse(matrix, links, message);
    }

    public static class ResponseMessages {
        public static final String MATRIX_NOT_FOUND = "Matrix not found.";
        public static final String MATRIX_ROTATED_SUCCESSFULLY = "Matrix rotated successfully.";
        public static final String MATRIX_STORED_AND_ROTATED = "Matrix stored and rotated successfully.";
        public static final String MATRIX_DEGREE_MULTIPLE_OF_90 = "Degree must be a multiple of 90.";
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