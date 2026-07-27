package br.ufpb.dcx.iago.lojadejogos.backend.security;

import br.ufpb.dcx.iago.lojadejogos.backend.config.SecurityConfig;
import br.ufpb.dcx.iago.lojadejogos.backend.controller.JogoController;
import br.ufpb.dcx.iago.lojadejogos.backend.service.JogoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JogoController.class)
@Import(SecurityConfig.class)
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JogoService jogoService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void deveRetornarForbiddenAoTentarDeletarJogoSemEstarAutenticado() throws Exception {
        mockMvc.perform(delete("/api/v1/jogos/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deveRetornarForbiddenAoTentarCriarJogoSendoApenasUser() throws Exception {
        mockMvc.perform(post("/api/v1/jogos")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void devePermitirAcessoAoCriarJogoSendoAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/jogos")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}