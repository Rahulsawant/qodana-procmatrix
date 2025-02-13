package com.qodana.procmatrix.controller;

import com.procmatrix.entity.MatrixData;
import com.procmatrix.entity.MatrixRequest;
import com.procmatrix.entity.MatrixResponse;
import com.procmatrix.utils.InputValidator;
import com.qodana.procmatrix.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/matrix")
public class MatrixController {

    @Autowired
    private MatrixService matrixService;

    /**
     * Retrieves a matrix by its ID.
     * @param id the ID of the matrix
     * @return the matrix data wrapped in an EntityModel with HATEOAS links
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatrixResponse> getMatrix(@PathVariable(name="id") Long id) {
        InputValidator.validateId(id);
        int[][] matrix = matrixService.getMatrix(id);
        if (matrix == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixController.class).getMatrix(id)).withSelfRel());
        MatrixResponse response = new MatrixResponse(matrix, links,null);
        return ResponseEntity.ok(response);
    }

    /**
     * Saves a matrix with the given ID
     * @param matrix the matrix data
     * @return a confirmation message wrapped in an EntityModel with HATEOAS links
     */
    @PostMapping
    public ResponseEntity<MatrixResponse> saveMatrix(@RequestBody MatrixRequest matrix) {
        try {
            InputValidator.validateMatrixRequest(matrix);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MatrixResponse(null, null,e.getMessage()));
        }

        MatrixData matrixData=matrixService.saveMatrix(matrix);
        if(matrixData == null){
            return ResponseEntity.internalServerError().build();
        }
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatrixController.class).getMatrix(matrixData.getId())).withSelfRel());
        MatrixResponse response = new MatrixResponse(null, links,null);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a matrix by its ID.
     *
     * @param id the ID of the matrix
     * @return a confirmation message wrapped in an EntityModel with HATEOAS links
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatrix(@PathVariable(name="id") Long id) {
        InputValidator.validateId(id);
        if(matrixService.deleteMatrix(id)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
