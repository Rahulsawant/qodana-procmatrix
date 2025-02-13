package com.qodana.procmatrix.controller;

import com.procmatrix.utils.InputValidator;
import com.qodana.procmatrix.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matrix")
public class MatrixAdditionController {
    @Autowired
    private MatrixService matrixService;

    /**
     * Adds two matrices and returns the result.
     *
     * @param id1 the ID of the first matrix
     * @param id2 the ID of the second matrix
     * @return the resulting matrix wrapped in an EntityModel with HATEOAS links
     */
    @GetMapping("/add/{id1}/{id2}")
    public EntityModel<int[][]> addMatrices(@PathVariable Long id1, @PathVariable Long id2) {
        InputValidator.validateId(id1);
        InputValidator.validateId(id2);

        int[][] matrix1 = matrixService.getMatrix(id1);
        int[][] matrix2 = matrixService.getMatrix(id2);
        int[][] result = addMatrices(matrix1, matrix2);
        EntityModel<int[][]> resource = EntityModel.of(result);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixAdditionController.class).addMatrices(id1, id2)).withSelfRel());
        return resource;
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
