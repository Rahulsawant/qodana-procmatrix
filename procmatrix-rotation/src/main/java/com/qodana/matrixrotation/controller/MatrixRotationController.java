package com.qodana.matrixrotation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procmatrix.entity.MatrixResponse;
import com.qodana.matrixrotation.service.MatrixRotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/matrix")
public class MatrixRotationController {

    @Autowired
    private MatrixRotationService matrixRotationService;

    /**
     * Rotates a matrix by 90 degrees clockwise.
     *
     * @param id the ID of the matrix
     * @return the rotated matrix wrapped in an EntityModel with HATEOAS links
     */
    /*@GetMapping("/rotate/{id}")
    public ResponseEntity<EntityModel<int[][]>> rotateMatrix(@PathVariable Long id) {
        int[][] matrix = matrixRotationService.getMatrix(id);
        int[][] rotatedMatrix = rotateMatrix(matrix);
        EntityModel<int[][]> resource = EntityModel.of(rotatedMatrix);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id)).withSelfRel());
        return new ResponseEntity<>(resource, org.springframework.http.HttpStatus.OK);
    }*/

    @GetMapping("/rotate/{id}")
    public ResponseEntity<MatrixResponse> rotateMatrix(@PathVariable(name="id") Long id) {
        int[][] matrix = matrixRotationService.getMatrix(id);
        int[][] rotatedMatrix = rotateMatrix(matrix);
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixRotationController.class).rotateMatrix(id)).withSelfRel());
        MatrixResponse response = new MatrixResponse(rotatedMatrix, links,null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private int[][] rotateMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotatedMatrix = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedMatrix[j][rows - 1 - i] = matrix[i][j];
            }
        }
        return rotatedMatrix;
    }
}