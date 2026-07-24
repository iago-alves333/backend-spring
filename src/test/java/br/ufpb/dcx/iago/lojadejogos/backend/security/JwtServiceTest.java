package br.ufpb.dcx.iago.lojadejogos.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para o JwtService.
 * Valida a geração e extração de informações de tokens JWT.
 */
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    // Instância real do serviço — não precisa de mock pois não tem dependências externas
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Injeta os valores que normalmente viriam do application.properties via @Value
        ReflectionTestUtils.setField(jwtService, "secret",
                "minha-chave-secreta-de-teste-com-256-bits-minimo!");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L); // 1 hora
    }

    @Test
    void deveGerarTokenValido() {
        // Gera token para um usuário de teste
        String token = jwtService.gerarToken("usuario@teste.com", "USER");

        // Garante que o token não é nulo e não é vazio
        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
        // Um JWT sempre tem 3 partes separadas por ponto
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void deveExtrairUsernameDoTokenComSucesso() {
        String emailOriginal = "usuario@teste.com";

        // Gera o token de teste
        String token = jwtService.gerarToken(emailOriginal, "USER");

        // Chama o método de extração
        String emailExtraido = jwtService.extrairEmail(token);

        // Valida se o email/username extraído confere com o original
        assertThat(emailExtraido).isEqualTo(emailOriginal);
    }

    @Test
    void deveValidarTokenCorretamente() {
        String token = jwtService.gerarToken("admin@teste.com", "ADMIN");

        // Token recém gerado deve ser válido
        assertThat(jwtService.isTokenValido(token)).isTrue();
    }

    @Test
    void deveExtrairRoleDoToken() {
        String token = jwtService.gerarToken("admin@teste.com", "ADMIN");

        String role = jwtService.extrairRole(token);

        assertThat(role).isEqualTo("ADMIN");
    }
}
