package com.procmatrix.client.service;

import com.procmatrix.api.MatrixApiApi;
import com.procmatrix.invoker.ApiException;
import com.procmatrix.model.MatrixAdditionRequest;
import com.procmatrix.model.MatrixRequest;
import com.procmatrix.model.MatrixResponse;
import com.procmatrix.rotation.api.MatrixRotationApiApi;
import com.procmatrix.rotation.model.RotateMatrixRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatrixClientService {

    private final MatrixApiApi matrixApi;
    private final MatrixRotationApiApi matrixRotationApi;

    @Autowired
    public MatrixClientService(MatrixApiApi matrixApi, MatrixRotationApiApi matrixRotationApi) {
        this.matrixApi = matrixApi;
        this.matrixRotationApi = matrixRotationApi;
    }

    public MatrixResponse getMatrix(Long matrixId) throws ApiException {
        return matrixApi.getMatrix(matrixId);
    }

    public MatrixResponse saveMatrix(int[][] matrixData) throws ApiException {
        MatrixRequest request = new MatrixRequest();
        request.setMatrix(convertArrayToList(matrixData));
        return matrixApi.saveMatrix(request);
    }

    public void deleteMatrix(Long matrixId) throws ApiException {
        matrixApi.deleteMatrix(matrixId);
    }

    public MatrixResponse addMatrices(Long matrix1Id, Long matrix2Id) throws ApiException {
        return matrixApi.addMatricesByIds(matrix1Id, matrix2Id);
    }

    public MatrixResponse addMatricesFromBody(int[][] matrix1, int[][] matrix2) throws ApiException {
        MatrixAdditionRequest request = new MatrixAdditionRequest();
        MatrixRequest matrixRequest1 = new MatrixRequest();
        matrixRequest1.setMatrix(convertArrayToList(matrix1));
        request.setMatrix1(matrixRequest1);

        MatrixRequest matrixRequest2 = new MatrixRequest();
        matrixRequest2.setMatrix(convertArrayToList(matrix2));
        request.setMatrix2(matrixRequest2);

        return matrixApi.addMatrices(request);
    }

    public com.procmatrix.rotation.model.MatrixResponse rotateMatrixById(Long matrixId, int degree)  {
        try {
            return matrixRotationApi.rotateMatrixById(matrixId, degree);
        } catch (com.procmatrix.rotation.invoker.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public com.procmatrix.rotation.model.MatrixResponse rotateMatrixFromBody(int[][] matrixData, int degree)  {
        RotateMatrixRequest request = new RotateMatrixRequest();
        request.setMatrix(convertArrayToList(matrixData));
        request.setDegree(degree);
        try {
            return matrixRotationApi.rotateMatrix(request);
        } catch (com.procmatrix.rotation.invoker.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private List<List<Integer>> convertArrayToList(int[][] array) {
        List<List<Integer>> list = new ArrayList<>();
        for (int[] row : array) {
            List<Integer> rowList = new ArrayList<>();
            for (int value : row) {
                rowList.add(value);
            }
            list.add(rowList);
        }
        return list;
    }
}