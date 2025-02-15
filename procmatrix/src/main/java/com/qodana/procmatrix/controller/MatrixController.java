package com.qodana.procmatrix.controller;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.core.entity.MatrixResponse;
import com.procmatrix.core.utils.InputValidator;
import com.qodana.procmatrix.service.MatrixService;
import com.qodana.procmatrix.utils.MatrixResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.qodana.procmatrix.utils.MatrixResponseBuilder.ResponseMessages.*;

@RestController
@RequestMapping("/api/matrix")
public class MatrixController {

    @Autowired
    private MatrixService matrixService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CREATE') OR hasRole('READ') OR hasRole('OPERATIONS')")
    public ResponseEntity<MatrixResponse> getMatrix(@PathVariable(name="id") Long id) {
        InputValidator.validateId(id);
        int[][] matrix = matrixService.getMatrix(id);
        if (matrix == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MatrixResponseBuilder.buildMatrixResponse(null, id, MATRIX_NOT_FOUND));
        }
        return ResponseEntity.ok(MatrixResponseBuilder.buildMatrixResponse(matrix, id, MATRIX_RETRIEVED_SUCCESSFULLY));
    }

    @PostMapping
    @PreAuthorize("hasRole('CREATE')")
    public ResponseEntity<MatrixResponse> saveMatrix(@RequestBody MatrixRequest matrix) {
        try {
            InputValidator.validateMatrixRequest(matrix);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MatrixResponse(null, null, e.getMessage()));
        }

        MatrixData matrixData = matrixService.saveMatrix(matrix);
        if (matrixData == null) {
            return ResponseEntity.internalServerError().body(MatrixResponseBuilder.buildMatrixResponse(null, null, FAILED_TO_SAVE_MATRIX));
        }
        return ResponseEntity.ok(MatrixResponseBuilder.buildMatrixResponse(null, matrixData.getId(), MATRIX_SAVED_SUCCESSFULLY));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CREATE')")
    public ResponseEntity<String> deleteMatrix(@PathVariable(name="id") Long id) {
        InputValidator.validateId(id);
        if (matrixService.deleteMatrix(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}