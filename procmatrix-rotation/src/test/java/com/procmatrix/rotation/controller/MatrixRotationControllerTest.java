
package com.procmatrix.rotation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.rotation.config.TestSecurityConfig;
import com.procmatrix.rotation.entity.RotateMatrixRequest;
import com.procmatrix.rotation.service.MatrixRotationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(MatrixRotationController.class)
@Import(TestSecurityConfig.class)
@ComponentScan(basePackages = "com.qodana.matrixrotation.*")
class MatrixRotationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatrixRotationService matrixRotationService;

    @Test
    @WithMockUser(username = "operator", roles = { "OPERATIONS"})
    void testRotateMatrix() throws Exception {
        int[][] matrix = {{1, 2}, {3, 4}};
        int[][] rotatedMatrix = {{3, 1}, {4, 2}};
        when(matrixRotationService.getMatrix(1L)).thenReturn(matrix);
        when(matrixRotationService.rotateMatrix(matrix, 90)).thenReturn(rotatedMatrix);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/matrix/rotate/1")
                        .param("degree", "90"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][0]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][1]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][0]").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][1]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].rel").value("rotate-90"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].href").value("http://localhost/api/matrix/rotate/1?degree=90"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[1].rel").value("rotate-180"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[1].href").value("http://localhost/api/matrix/rotate/1?degree=180"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[2].rel").value("rotate-360"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[2].href").value("http://localhost/api/matrix/rotate/1?degree=360"));

    }

    @Test
    @WithMockUser(username = "operator", roles = { "CREATE", "OPERATIONS"})
    void rotateMatrix_ValidRequest_ReturnsRotatedMatrix() throws Exception {

        RotateMatrixRequest request = new RotateMatrixRequest();
        request.setMatrix(new int[][]{{1, 2}, {3, 4}});
        request.setDegree(90);
        int[][] rotatedMatrix = {{3, 1}, {4, 2}};
        MatrixData matrixData = new MatrixData();
        matrixData.setId(1L);
        matrixData.setMatrix(request.getMatrix());

        when(matrixRotationService.saveMatrix(ArgumentMatchers.any(MatrixRequest.class))).thenReturn(matrixData);
        when(matrixRotationService.rotateMatrix(ArgumentMatchers.any(int[][].class), ArgumentMatchers.eq(request.getDegree()))).thenReturn(rotatedMatrix);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/matrix/rotate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][0]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][1]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][0]").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][1]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].rel").value("rotate-90"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].href").value("http://localhost/api/matrix/rotate/1?degree=90"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[1].rel").value("rotate-180"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[1].href").value("http://localhost/api/matrix/rotate/1?degree=180"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[2].rel").value("rotate-360"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[2].href").value("http://localhost/api/matrix/rotate/1?degree=360"));
    }

    @Test
    @WithMockUser(username = "operator", roles = { "CREATE", "OPERATIONS"})
    void rotateMatrix_InvalidRequest_ReturnsBadRequest() throws Exception {
        RotateMatrixRequest request = new RotateMatrixRequest();
        request.setMatrix(new int[][]{{1, 2}, {3}});
        request.setDegree(90);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/matrix/rotate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void unauthorizedAccess_Returns401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/matrix/rotate/1")
                        .param("degree", "90"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "operator", roles = { "OPERATIONS"})
    void rotateMatrix_InvalidDegree_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/matrix/rotate/1")
                        .param("degree", "45"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "operator", roles = { "OPERATIONS"})
    void rotateMatrix_InternalServerError() throws Exception {
        when(matrixRotationService.getMatrix(1L)).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/matrix/rotate/1")
                        .param("degree", "90"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}