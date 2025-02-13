// File: src/test/java/com/qodana/matrixrotation/controller/MatrixRotationControllerTest.java

package com.qodana.matrixrotation.controller;

import com.qodana.matrixrotation.config.TestSecurityConfig;
import com.qodana.matrixrotation.service.MatrixRotationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(MatrixRotationController.class)
@Import(TestSecurityConfig.class)
@ComponentScan(basePackages = "com.qodana.matrixrotation.*")
public class MatrixRotationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatrixRotationService matrixRotationService;

    @Test
    public void testRotateMatrix() throws Exception {
        int[][] matrix = {{1, 2}, {3, 4}};
        int[][] rotatedMatrix = {{3, 1}, {4, 2}};
        when(matrixRotationService.getMatrix(1L)).thenReturn(matrix);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/matrix/rotate/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][0]").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0][1]").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][0]").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1][1]").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].rel").value("self"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.links[0].href").value("http://localhost/api/matrix/rotate/1"));

    }
}