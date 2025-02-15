package com.qodana.procmatrix.utils;

import com.procmatrix.core.entity.MatrixResponse;
import com.qodana.procmatrix.controller.MatrixAdditionController;
import com.qodana.procmatrix.controller.MatrixController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;

public class MatrixResponseBuilder {

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
}