package com.procmatrix.core.utils;

import com.procmatrix.core.entity.MatrixRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class InputValidatorTest {

    @Test
    void testValidateMatrixRequest_NullRequest() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateMatrixRequest(null));
    }

    @Test
    void testValidateMatrixRequest_NullMatrix() {
        MatrixRequest request = new MatrixRequest();
        request.setMatrix(null);
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateMatrixRequest(request));
    }

    @Test
    void testValidateMatrixRequest_EmptyMatrix() {
        MatrixRequest request = new MatrixRequest();
        request.setMatrix(new int[0][0]);
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateMatrixRequest(request));
    }

    @Test
    void testValidateMatrixRequest_RowsLengthMismatch() {
        MatrixRequest request = new MatrixRequest();
        request.setMatrix(new int[][]{{1, 2}, {3}});
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateMatrixRequest(request));
    }

    @Test
    void testValidateMatrixRequests_ValidRequests() {
        MatrixRequest request1 = new MatrixRequest();
        request1.setMatrix(new int[][]{{1, 2}, {3, 4}});
        MatrixRequest request2 = new MatrixRequest();
        request2.setMatrix(new int[][]{{5, 6}, {7, 8}});
        InputValidator.validateMatrixRequests(request1, request2);
    }

    @Test
    void testValidateId_NullId() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateId(null));
    }

    @Test
    void testValidateId_InvalidId() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateId(-1L));
    }

    @Test
    void testValidateIds_ValidIds() {
        InputValidator.validateIds(1L, 2L, 3L);
    }

    @Test
    void testValidateIds_InvalidIds() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateIds(1L, -2L, 3L));
    }
}