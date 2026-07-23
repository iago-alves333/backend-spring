package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    // TODO: Criar mock para UserService
    // TODO: Criar mock para JwtService
    // TODO: Configurar o MockMvc
    
    @Test
    void testLoginComSucesso() {
        // TODO: Simular requisição POST /login com credenciais válidas
        // TODO: Verificar se o token é retornado com status 200 OK
    }

    @Test
    void testLoginComCredenciaisInvalidas() {
        // TODO: Simular requisição POST /login com senha ou email errados
        // TODO: Verificar se lança exceção ou retorna status 401 Unauthorized
    }
}
