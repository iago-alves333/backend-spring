package br.ufpb.dcx.iago.lojadejogos.backend.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    // TODO: Instanciar o JwtService

    @Test
    void deveGerarTokenValido() {
        // TODO: Criar mock de um UserDetails / User
        // TODO: Chamar o JwtService para gerar um token
        // TODO: Garantir que o token não é nulo e não é vazio
    }

    @Test
    void deveExtrairUsernameDoTokenComSucesso() {
        // TODO: Gerar o token de teste e chamar o método de extração
        // TODO: Validar se o email/username extraído confere com o original
    }
}
