package com.sofka.customerservice.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateCliente() throws Exception {
        String body = """
                {
                  "clienteId": 1,
                  "nombre": "Jose Lema",
                  "genero": "Masculino",
                  "edad": 30,
                  "identificacion": "1234567890",
                  "direccion": "Otavalo sn y principal",
                  "telefono": "098254785",
                  "contrasena": "1234",
                  "estado": true
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.nombre").value("Jose Lema"));
    }

    @Test
    void shouldListClientes() throws Exception {
        String body = """
                {
                  "clienteId": 2,
                  "nombre": "Marianela Montalvo",
                  "genero": "Femenino",
                  "edad": 28,
                  "identificacion": "9876543210",
                  "direccion": "Amazonas y NNUU",
                  "telefono": "097548965",
                  "contrasena": "5678",
                  "estado": true
                }
                """;

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].clienteId").value(2));
    }
}
