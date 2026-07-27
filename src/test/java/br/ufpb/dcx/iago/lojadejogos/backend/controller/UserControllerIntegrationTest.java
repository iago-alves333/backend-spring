package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.EmailJaCadastradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.security.JwtService;
import br.ufpb.dcx.iago.lojadejogos.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @Test
    void deveRetornarBadRequestQuandoEmailInvalido() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNome("Maria");
        dto.setEmail("email-invalido"); // Falha no @Email
        dto.setSenha("123456");
        dto.setSaldo(BigDecimal.ZERO);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").isArray())
                .andExpect(jsonPath("$.mensagem").value("Erro de validação nos dados enviados."));
    }

    @Test
    void deveRetornarConflictQuandoEmailJaEstiverEmUso() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNome("Maria");
        dto.setEmail("maria@teste.com");
        dto.setSenha("123456");
        dto.setSaldo(BigDecimal.ZERO);

        when(userService.salvar(any(UserRequestDTO.class)))
                .thenThrow(new EmailJaCadastradoException("maria@teste.com"));

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem", org.hamcrest.Matchers.containsString("maria@teste.com")));
    }
}