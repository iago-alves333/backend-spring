package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.PrecoInvalidoException;
import br.ufpb.dcx.iago.lojadejogos.backend.security.JwtService;
import br.ufpb.dcx.iago.lojadejogos.backend.service.JogoService;
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

@WebMvcTest(JogoController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita os filtros de segurança para focar no @Valid e ExceptionHandler
public class JogoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JogoService jogoService;

    @MockitoBean
    private JwtService jwtService; // Necessário para carregar o contexto devido ao filtro de segurança

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @Test
    void deveRetornarBadRequestQuandoNomeDoJogoForVazioOuNulo() throws Exception {
        JogoRequestDTO dto = new JogoRequestDTO();
        dto.setPreco(BigDecimal.valueOf(100.0));
        dto.setTipo("Ação");
        dto.setDescricao("Desc");

        mockMvc.perform(post("/api/v1/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").isArray())
                .andExpect(jsonPath("$.mensagem").value("Erro de validação nos dados enviados."));
    }

    // RENOMEADO: o nome antigo (deveRetornarUnprocessableEntityQuandoPrecoForInvalido)
    // dizia 422, mas o teste sempre verificou 400.
    @Test
    void deveRetornarBadRequestQuandoPrecoForInvalidoNaValidacaoDoDTO() throws Exception {
        JogoRequestDTO dto = new JogoRequestDTO();
        dto.setNome("Cyberpunk");
        dto.setPreco(BigDecimal.valueOf(-10.0));
        dto.setTipo("Ação");
        dto.setDescricao("Desc");

        // O Spring MVC também valida o @DecimalMin antes de chegar no Service.
        // Portanto, aqui vai dar erro.
        mockMvc.perform(post("/api/v1/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0]").value(org.hamcrest.Matchers.containsString("Não pode ser negativo")));
    }

    @Test
    void deveRetornarUnprocessableEntityQuandoServiceLancarExcecaoPrecoInvalido() throws Exception {
        JogoRequestDTO dto = new JogoRequestDTO();
        dto.setNome("Cyberpunk");
        dto.setPreco(BigDecimal.valueOf(10.0));
        dto.setTipo("Ação");
        dto.setDescricao("Desc");

        // Simula que a validação passou pelo DTO, mas o service lançou a exceção de negócio
        when(jogoService.salvar(any(JogoRequestDTO.class)))
                .thenThrow(new PrecoInvalidoException("O preço não pode ser negativo no service"));

        mockMvc.perform(post("/api/v1/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.mensagem").value("O preço não pode ser negativo no service"));
    }
}