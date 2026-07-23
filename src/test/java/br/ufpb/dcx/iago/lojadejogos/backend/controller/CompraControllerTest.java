package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(CompraController.class)
public class CompraControllerTest {

    // TODO: Criar mock para CompraService
    // TODO: Configurar o MockMvc

    @Test
    void testRealizarCompraComSucesso() {
        // TODO: Simular requisição POST para efetuar compra de um jogo
        // TODO: Verificar o retorno da CompraResponseDTO com status 201 Created
    }

    @Test
    void testRealizarCompraSaldoInsuficiente() {
        // TODO: Simular cenário em que o usuário não tem saldo
        // TODO: Verificar se o controlador trata a SaldoInsuficienteException com status 400 ou 422
    }
}
