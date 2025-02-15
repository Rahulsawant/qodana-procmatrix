package com.qodana.matrixrotation.controller;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.core.utils.InputValidator;
import com.qodana.matrixrotation.entity.RotateMatrixRequest;
import com.qodana.matrixrotation.service.MatrixRotationService;
import com.qodana.matrixrotation.utils.MatrixResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.qodana.matrixrotation.utils.MatrixResponseBuilder.ResponseMessages.*;


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

    @GetMapping("/rotate/{id}")
    @PreAuthorize("hasRole('CREATE') or hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> rotateMatrix(@PathVariable(name="id") Long id, @RequestParam(name="degree", defaultValue="90") int degree) {
        if (degree % 90 != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MatrixResponseBuilder.buildMatrixResponse(null, id, MATRIX_DEGREE_MULTIPLE_OF_90));
        }
        int[][] matrix = matrixRotationService.getMatrix(id);
        int[][] rotatedMatrix = matrixRotationService.rotateMatrix(matrix, degree);

        //TODO rotated matrix can be store in the cache first , & if it is being accessed frequently then store in the DB

        MatrixResponse response = MatrixResponseBuilder.buildMatrixResponse(rotatedMatrix, id, MATRIX_ROTATED_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/rotate")
    public ResponseEntity<MatrixResponse> rotateMatrix(@RequestBody RotateMatrixRequest matrixRequest) {

        try {
            InputValidator.validateMatrixRequest(matrixRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MatrixResponse(null, null,e.getMessage()));
        }

        MatrixData matrixData=matrixRotationService.saveMatrix(matrixRequest);
        if(matrixData == null){
            return ResponseEntity.internalServerError().build();
        }

        int[][] rotatedMatrix = matrixRotationService.rotateMatrix(matrixRequest.getMatrix(), matrixRequest.getDegree());
        MatrixResponse response = MatrixResponseBuilder.buildMatrixResponse(rotatedMatrix, matrixData.getId(), MATRIX_STORED_AND_ROTATED);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}