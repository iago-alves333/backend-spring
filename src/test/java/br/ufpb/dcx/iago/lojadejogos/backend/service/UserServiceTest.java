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
 * Valida as regras de criação de usuário, unicidade de email e atualização de dados básicos.
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

    // CORREÇÃO: o teste anterior (deveAdicionarSaldoAoUsuarioComSucesso) era um
    // falso positivo. Ele setava o saldo esperado manualmente no objeto ANTES de
    // chamar o serviço e fazia o mock de save() devolver um objeto já pronto com
    // o saldo certo — ou seja, passava independentemente do que atualizar() faz.
    @Test
    void naoDeveAlterarSaldoAoAtualizarDadosBasicos() {
        User usuario = criarUserSalvo();
        BigDecimal saldoOriginal = usuario.getSaldo();

        UserRequestDTO dto = criarUserRequestDTO();
        dto.setSaldo(BigDecimal.valueOf(999.00));

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(userRepository.save(any(User.class))).thenReturn(usuario);

        UserResponseDTO resposta = userService.atualizar(1L, dto);

        // O saldo deve permanecer o original, atualizar() não mexe nesse campo
        assertThat(resposta.getSaldo()).isEqualByComparingTo(saldoOriginal);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.buscarPorId(99L))
                .isInstanceOf(br.ufpb.dcx.iago.lojadejogos.backend.exception.UsuarioNaoEncontradoException.class);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deletarPorId(99L))
                .isInstanceOf(br.ufpb.dcx.iago.lojadejogos.backend.exception.UsuarioNaoEncontradoException.class);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioComEmailDeOutro() {
        User usuarioOriginal = criarUserSalvo(); // id 1, joao@teste.com
        UserRequestDTO dto = criarUserRequestDTO();
        dto.setEmail("maria@teste.com"); // Email que já pertence a outra pessoa

        User usuarioExistente = new User();
        usuarioExistente.setId(2L);
        usuarioExistente.setEmail("maria@teste.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioOriginal));
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuarioExistente));

        assertThatThrownBy(() -> userService.atualizar(1L, dto))
                .isInstanceOf(EmailJaCadastradoException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        User usuarioOriginal = criarUserSalvo();
        UserRequestDTO dto = criarUserRequestDTO();
        dto.setNome("Nome Atualizado");

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioOriginal));
        when(userRepository.save(any(User.class))).thenReturn(usuarioOriginal);

        UserResponseDTO resposta = userService.atualizar(1L, dto);

        assertThat(resposta.getNome()).isEqualTo("Nome Atualizado");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveListarJogosDoUsuarioComSucesso() {
        User usuario = criarUserSalvo();
        br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo jogo = new br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo();
        jogo.setId(10L);
        jogo.setNome("The Witcher");
        usuario.setJogos(java.util.List.of(jogo));

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));

        java.util.List<br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO> jogos = userService.listarJogosDOUsuario(1L);

        assertThat(jogos).hasSize(1);
        assertThat(jogos.get(0).getNome()).isEqualTo("The Witcher");
    }

    @Test
    void deveListarJogosDoUsuarioVazio() {
        User usuario = criarUserSalvo();
        usuario.setJogos(new java.util.ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));

        java.util.List<br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO> jogos = userService.listarJogosDOUsuario(1L);

        assertThat(jogos).isEmpty();
    }
}