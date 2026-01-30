package com.lucasteixeira.bank.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasteixeira.bank.application.dtos.UserDTO;
import com.lucasteixeira.bank.application.services.UserService;
import com.lucasteixeira.bank.infratructure.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private String email;

    @BeforeEach
    void setUp() {
        email = "teste@teste.com";
        userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setName("Lucas Teixeira");
        userDTO.setEmail(email);
        userDTO.setPassword("123456");
        userDTO.setCpf("705.484.450-52");
    }

    @Test
    @DisplayName("Deve criar um usuário e retornar 201 Created")
    void createUser_ReturnsCreated() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando o CPF for inválido")
    void createUser_ReturnsBadRequest_InvalidCpf() throws Exception {
        userDTO.setCpf("000.000.000-00");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de validação"));
    }

    @Test
    @DisplayName("Deve fazer login e retornar token 200 OK")
    void loginWithEmail_ReturnsOk() throws Exception {
        when(userService.loginUser(any(UserDTO.class))).thenReturn("token-jwt-fake");

        mockMvc.perform(post("/user/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("token-jwt-fake"));
    }

    @Test
    @WithMockUser(username = "teste@teste.com")
    @DisplayName("Deve buscar informações do usuário logado 200 OK")
    void getMyInfoByEmail_ReturnsOk() throws Exception {
        when(userService.getUserInfoByEmail(anyString())).thenReturn(userDTO);

        mockMvc.perform(get("/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @WithMockUser(username = "teste@teste.com")
    @DisplayName("Deve atualizar usuário completo 200 OK")
    void updateUser_ReturnsOk() throws Exception {
        when(userService.updateUser(any(UserDTO.class), anyString())).thenReturn(userDTO);

        mockMvc.perform(put("/user/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lucas Teixeira"));
    }

    @Test
    @WithMockUser(username = "teste@teste.com")
    @DisplayName("Deve atualizar senha 200 OK")
    void updatePassword_ReturnsOk() throws Exception {
        when(userService.updatePassword(any(UserDTO.class), anyString())).thenReturn(userDTO);

        mockMvc.perform(patch("/user/update-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teste@teste.com")
    @DisplayName("Deve atualizar atividade 200 OK")
    void updateActivity_ReturnsOk() throws Exception {
        when(userService.updateActivity(any(UserDTO.class), anyString())).thenReturn(userDTO);

        mockMvc.perform(patch("/user/update-activity")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teste@teste.com")
    @DisplayName("Deve atualizar conta parcialmente 200 OK")
    void updateAccountPartial_ReturnsOk() throws Exception {
        when(userService.updateAccountPartial(any(UserDTO.class), anyString())).thenReturn(userDTO);

        mockMvc.perform(patch("/user/update-user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lucas Teixeira"));
    }

    @Test
    @WithMockUser(username = "teste@teste.com")
    @DisplayName("Deve deletar conta e retornar mensagem 200 OK")
    void deleteAccount_ReturnsOk() throws Exception {
        when(userService.deleteAccount(anyString())).thenReturn("Usuário deletado com sucesso");

        mockMvc.perform(delete("/user/delete-account")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado com sucesso"));
    }

    @Test
    @WithMockUser(username = "teste@teste.com")
    @DisplayName("Deve retornar 404 quando o serviço lançar ResourceNotFoundException")
    void handleNotFound_Returns404() throws Exception {
        when(userService.getUserInfoByEmail(anyString())).thenThrow(new ResourceNotFoundException("Não encontrado"));

        mockMvc.perform(get("/user/me"))
                .andExpect(status().isNotFound());
    }
}