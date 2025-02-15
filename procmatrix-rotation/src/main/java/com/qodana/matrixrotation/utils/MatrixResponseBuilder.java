package com.qodana.matrixrotation.utils;

import com.procmatrix.core.entity.MatrixResponse;
import com.qodana.matrixrotation.controller.MatrixRotationController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;

public class MatrixResponseBuilder {

    public static MatrixResponse buildMatrixResponse(int[][] matrix, Long id, String message) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id, 90)).withRel("rotate-90"));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id, 180)).withRel("rotate-180"));
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id, 360)).withRel("rotate-360"));
        return new MatrixResponse(matrix, links, message);
    }

    public static class ResponseMessages {
        public static final String MATRIX_DEGREE_MULTIPLE_OF_90 = "Degree must be a multiple of 90.";
        public static final String MATRIX_ROTATED_SUCCESS = "Matrix rotated successfully. Use the provided links to rotate the matrix by the desired degree.";
        public static final String MATRIX_STORED_AND_ROTATED = "Matrix stored & rotated. Use the provided links to rotate the matrix by the desired degree.";
    }
}