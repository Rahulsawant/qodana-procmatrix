package com.procmatrix.controller;

import com.procmatrix.core.entity.MatrixData;
import com.procmatrix.core.entity.MatrixRequest;
import com.procmatrix.config.TestSecurityConfig;
import com.procmatrix.service.MatrixService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;


@WebMvcTest(MatrixController.class)
@Import(TestSecurityConfig.class)
@ComponentScan(basePackages = "com.procmatrix.*")
 class MatrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatrixService matrixService;


    @Test
    @WithMockUser(username = "reader", roles = { "READ"})
     void getMatrixReturnsMatrixWithValidId() throws Exception {
        int[][] matrix = {{1, 2}, {3, 4}};
        when(matrixService.getMatrix(1L)).thenReturn(matrix);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/matrix/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][0]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][1]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][0]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][1]").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].rel").value("self"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].href").value("http://localhost/api/matrix/1"));
    }

    @Test
    @WithMockUser(username = "reader", roles = { "READ"})
     void getMatrixReturnsNotFoundForInvalidId() throws Exception {
        when(matrixService.getMatrix(999L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/matrix/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @WithMockUser(username = "creator", roles = {"CREATE"})
     void deleteMatrixDeletesMatrixWithValidId() throws Exception {
        when(matrixService.deleteMatrix(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/matrix/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATE"})
     void deleteMatrixReturnsNotFoundForInvalidId() throws Exception {
        when(matrixService.deleteMatrix(999L)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/matrix/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATE"})
     void saveMatrixReturnsOkForValidRequest() throws Exception {
        MatrixRequest matrixRequest = new MatrixRequest();
        matrixRequest.setMatrix(new int[][]{{1, 2}, {3, 4}});
        String jsonRequest = "{\"matrix\":[[1,2],[3,4]]}";

        MatrixData matrixData=new MatrixData();
        matrixData.setId(1L);
        matrixData.setMatrix(new int[][]{{1, 2}, {3, 4}});
        when(matrixService.saveMatrix(ArgumentMatchers.any(MatrixRequest.class))).thenReturn(matrixData);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/matrix")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].href").value("http://localhost/api/matrix/1"));
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATE"})
     void saveMatrixReturnsBadRequestForInvalidRequest() throws Exception {
        String jsonRequest = "{\"matrix\":null}"; // Invalid input

        mockMvc.perform(MockMvcRequestBuilders.post("/api/matrix")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "creator", roles = {"CREATE"})
     void saveMatrixReturnsInternalServerErrorWhenSaveFails() throws Exception {
        MatrixRequest matrixRequest = new MatrixRequest();
        matrixRequest.setMatrix(new int[][]{{1, 2}, {3, 4}});
        String jsonRequest = "{\"matrix\":[[1,2],[3,4]]}";

        when(matrixService.saveMatrix(matrixRequest)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/matrix")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
