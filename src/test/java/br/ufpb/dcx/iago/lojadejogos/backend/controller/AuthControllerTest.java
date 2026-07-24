package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.LoginRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para o AuthController.
 * Valida os fluxos de login com credenciais válidas e inválidas.
 *
 * Utiliza Mockito puro (sem servidor HTTP), injetando mocks diretamente
 * no controlador para isolar a lógica do endpoint de autenticação.
 */
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    /** Mock para UserRepository — simula a busca de usuário no banco */
    @Mock
    private UserRepository userRepository;

    /** Mock para JwtService — evita dependência de configuração real do JWT */
    @Mock
    private JwtService jwtService;

    /** Mock para PasswordEncoder — controla a verificação da senha */
    @Mock
    private PasswordEncoder passwordEncoder;

    /** Instância real do controller com mocks injetados */
    @InjectMocks
    private AuthController authController;

    private User usuarioValido;

    @BeforeEach
    void setUp() {
        usuarioValido = new User();
        usuarioValido.setId(1L);
        usuarioValido.setNome("Usuário Teste");
        usuarioValido.setEmail("usuario@teste.com");
        usuarioValido.setSenha("$2a$10$hashBcrypt");
        usuarioValido.setSaldo(BigDecimal.valueOf(100.00));
        usuarioValido.setAdmin(false);
    }

    @Test
    void testLoginComSucesso() {
        // Monta credenciais válidas
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("usuario@teste.com");
        request.setSenha("senha123");

        // Configura os mocks: repositório acha o usuário e a senha bate
        when(userRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuarioValido));
        when(passwordEncoder.matches("senha123", "$2a$10$hashBcrypt")).thenReturn(true);
        when(jwtService.gerarToken("usuario@teste.com", "USER")).thenReturn("meu.token.jwt");

        // Simula requisição POST /login com credenciais válidas
        ResponseEntity<?> resposta = authController.login(request);

        // Verifica se o token é retornado com status 200 OK
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isNotNull();
    }

    @Test
    void testLoginComCredenciaisInvalidas() {
        // Credenciais com senha errada
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("usuario@teste.com");
        request.setSenha("senhaErrada");

        // Repositório acha o usuário, mas a senha NÃO bate
        when(userRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuarioValido));
        when(passwordEncoder.matches("senhaErrada", "$2a$10$hashBcrypt")).thenReturn(false);

        // Simula requisição POST /login com senha ou email errados
        ResponseEntity<?> resposta = authController.login(request);

        // Verifica se o controlador retorna status 401 Unauthorized
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testLoginComEmailInexistente() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("naoexiste@teste.com");
        request.setSenha("senha123");

        // Repositório não encontra nenhum usuário com esse email
        when(userRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        ResponseEntity<?> resposta = authController.login(request);

        // Sem usuário, deve retornar 401
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testLoginComUsuarioAdmin() {
        usuarioValido.setAdmin(true);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("usuario@teste.com");
        request.setSenha("senha123");

        when(userRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuarioValido));
        when(passwordEncoder.matches("senha123", "$2a$10$hashBcrypt")).thenReturn(true);
        when(jwtService.gerarToken("usuario@teste.com", "ADMIN")).thenReturn("token.admin.jwt");

        ResponseEntity<?> resposta = authController.login(request);

        // Admin deve receber 200 com token gerado para role ADMIN
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
