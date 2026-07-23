package br.ufpb.dcx.iago.lojadejogos.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {

    // TODO: Criar @Mock para CompraRepository
    // TODO: Criar @Mock para UserRepository
    // TODO: Criar @Mock para JogoRepository
    // TODO: Criar @InjectMocks para CompraService

    @Test
    void deveEfetuarCompraComSucesso() {
        // TODO: Configurar mocks de usuário com saldo suficiente e jogo existente
        // TODO: Chamar o método do serviço
        // TODO: Verificar se os repositórios save() foram chamados
        // TODO: Validar se o saldo do usuário foi decrescido corretamente
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // TODO: Configurar mock de usuário com saldo menor que o valor do jogo
        // TODO: Chamar o método do serviço garantindo que lança SaldoInsuficienteException
        // TODO: Verificar se o método save() da compra NUNCA foi chamado
    }

    @Test
    void deveLancarExcecaoQuandoJogoNaoEncontrado() {
        // TODO: Configurar mock que retorna vazio para a busca do jogo
        // TODO: Verificar o lançamento de JogoNaoEncontradoException
    }

    @Test
    void deveLancarExcecaoQuandoJogoJaAdquirido() {
        // TODO: Configurar mock informando que o usuário já possui aquele jogo
        // TODO: Verificar o lançamento de JogoJaAdquiridoException
    }
}
