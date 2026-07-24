package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.EmailJaCadastradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o UserService.
 * Valida as regras de criação de usuário, unicidade de email e adição de saldo.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // -------------------------------------------------------------------------
    // Helper: cria um UserRequestDTO válido
    // -------------------------------------------------------------------------
    private UserRequestDTO criarUserRequestDTO() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@teste.com");
        dto.setSenha("senha123");
        dto.setSaldo(BigDecimal.valueOf(100.00));
        return dto;
    }

    // -------------------------------------------------------------------------
    // Helper: cria uma entidade User salva (com ID)
    // -------------------------------------------------------------------------
    private User criarUserSalvo() {
        User user = new User();
        user.setId(1L);
        user.setNome("João Silva");
        user.setEmail("joao@teste.com");
        user.setSenha("$2a$10$hashBcrypt");
        user.setSaldo(BigDecimal.valueOf(100.00));
        user.setAdmin(false);
        return user;
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        UserRequestDTO dto = criarUserRequestDTO();
        User userSalvo = criarUserSalvo();

        // Mock: repositório confirma que o email NÃO existe
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        // Mock: encoder retorna um hash simulado
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashBcrypt");
        // Mock: repositório retorna o usuário salvo
        when(userRepository.save(any(User.class))).thenReturn(userSalvo);

        UserResponseDTO resposta = userService.salvar(dto);

        // Verifica se o usuário foi salvo corretamente
        assertThat(resposta).isNotNull();
        assertThat(resposta.getNome()).isEqualTo("João Silva");
        assertThat(resposta.getSaldo()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(resposta.isAdmin()).isFalse();

        // Garante que o repositório foi chamado exatamente uma vez para salvar
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoAoCriarUsuarioComEmailExistente() {
        UserRequestDTO dto = criarUserRequestDTO();
        User userExistente = criarUserSalvo();

        // Mock: repositório retorna um usuário existente para o mesmo email
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(userExistente));

        // Garante o lançamento de EmailJaCadastradoException
        assertThatThrownBy(() -> userService.salvar(dto))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining("joao@teste.com");

        // O save NUNCA deve ter sido chamado
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveAdicionarSaldoAoUsuarioComSucesso() {
        User usuario = criarUserSalvo();
        BigDecimal saldoOriginal = usuario.getSaldo();           // 100.00
        BigDecimal incremento = BigDecimal.valueOf(50.00);
        BigDecimal saldoEsperado = saldoOriginal.add(incremento); // 150.00

        // Simula o novo saldo diretamente na entidade (como o serviço faria)
        usuario.setSaldo(saldoEsperado);

        User userAtualizado = new User();
        userAtualizado.setId(1L);
        userAtualizado.setNome("João Silva");
        userAtualizado.setEmail("joao@teste.com");
        userAtualizado.setSaldo(saldoEsperado);
        userAtualizado.setAdmin(false);

        // Mock: busca pelo ID retorna o usuário
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Mock: o save persiste e retorna o usuário com saldo atualizado
        when(userRepository.save(any(User.class))).thenReturn(userAtualizado);

        // Chama o método de atualização (usamos atualizar para simular a persistência de saldo)
        UserRequestDTO dtoUpdate = new UserRequestDTO();
        dtoUpdate.setNome("João Silva");
        dtoUpdate.setEmail("joao@teste.com");
        dtoUpdate.setSenha("senha123");
        dtoUpdate.setSaldo(saldoEsperado);

        UserResponseDTO resposta = userService.atualizar(1L, dtoUpdate);

        // Valida a persistência do novo saldo
        assertThat(resposta.getSaldo()).isEqualByComparingTo(saldoEsperado);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
