package br.ufpb.dcx.iago.lojadejogos.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // TODO: Criar @Mock para UserRepository
    // TODO: Criar @InjectMocks para UserService

    @Test
    void deveCriarUsuarioComSucesso() {
        // TODO: Configurar mock de repositório confirmando que o email não existe
        // TODO: Chamar método de criação de usuário
        // TODO: Verificar se o usuário foi salvo corretamente
    }

    @Test
    void deveLancarExcecaoAoCriarUsuarioComEmailExistente() {
        // TODO: Configurar mock para retornar true ao buscar pelo email
        // TODO: Chamar o método garantindo o lançamento de EmailJaCadastradoException
    }

    @Test
    void deveAdicionarSaldoAoUsuarioComSucesso() {
        // TODO: Buscar o usuário pelo ID via mock
        // TODO: Incrementar o saldo
        // TODO: Validar a persistência do novo saldo
    }
}
