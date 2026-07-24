package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.JogoJaAdquiridoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.JogoNaoEncontradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.SaldoInsuficienteException;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Compra;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.CompraRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.JogoRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o CompraService.
 * Valida as regras de negócio da compra: saldo, duplicidade e entidade não encontrada.
 */
@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JogoRepository jogoRepository;

    @InjectMocks
    private CompraService compraService;

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private User criarUsuarioComSaldo(BigDecimal saldo) {
        User user = new User();
        user.setId(1L);
        user.setNome("Maria");
        user.setEmail("maria@teste.com");
        user.setSaldo(saldo);
        user.setAdmin(false);
        user.setJogos(new ArrayList<>());
        return user;
    }

    private Jogo criarJogo(Long id, BigDecimal preco) {
        Jogo jogo = new Jogo();
        jogo.setId(id);
        jogo.setNome("The Witcher 3");
        jogo.setPreco(preco);
        jogo.setTipo("RPG");
        return jogo;
    }

    private CompraRequestDTO criarDto(Long userId, Long jogoId) {
        CompraRequestDTO dto = new CompraRequestDTO();
        dto.setUserId(userId);
        dto.setJogoId(jogoId);
        return dto;
    }

    // -------------------------------------------------------------------------
    // Testes
    // -------------------------------------------------------------------------

    @Test
    void deveEfetuarCompraComSucesso() {
        BigDecimal saldoInicial = BigDecimal.valueOf(200.00);
        BigDecimal precoJogo    = BigDecimal.valueOf(59.90);

        User usuario = criarUsuarioComSaldo(saldoInicial);
        Jogo jogo    = criarJogo(10L, precoJogo);

        // Configura mocks de usuário com saldo suficiente e jogo existente
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(jogoRepository.findById(10L)).thenReturn(Optional.of(jogo));

        // Mock do save de User (retorna o próprio usuário)
        when(userRepository.save(any(User.class))).thenReturn(usuario);

        // Mock do save de Compra
        Compra compraSalva = new Compra();
        compraSalva.setId(99L);
        compraSalva.setUser(usuario);
        compraSalva.setJogo(jogo);
        compraSalva.setDataCompra(LocalDateTime.now());
        compraSalva.setValorPago(precoJogo);
        when(compraRepository.save(any(Compra.class))).thenReturn(compraSalva);

        CompraRequestDTO dto = criarDto(1L, 10L);

        // Chama o método do serviço
        CompraResponseDTO resposta = compraService.realizarCompra(dto);

        // Verificações
        assertThat(resposta).isNotNull();
        assertThat(resposta.getNomeJogo()).isEqualTo("The Witcher 3");
        assertThat(resposta.getNomeUsuario()).isEqualTo("Maria");
        assertThat(resposta.getPreco()).isEqualByComparingTo(precoJogo);

        // Verifica se os repositórios save() foram chamados
        verify(userRepository, times(1)).save(any(User.class));
        verify(compraRepository, times(1)).save(any(Compra.class));

        // Valida se o saldo do usuário foi decrementado corretamente
        BigDecimal saldoEsperado = saldoInicial.subtract(precoJogo);
        assertThat(usuario.getSaldo()).isEqualByComparingTo(saldoEsperado);
    }

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // Usuário com saldo menor que o preço do jogo
        User usuario = criarUsuarioComSaldo(BigDecimal.valueOf(10.00));
        Jogo jogo    = criarJogo(10L, BigDecimal.valueOf(59.90));

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(jogoRepository.findById(10L)).thenReturn(Optional.of(jogo));

        CompraRequestDTO dto = criarDto(1L, 10L);

        // Garante que lança SaldoInsuficienteException
        assertThatThrownBy(() -> compraService.realizarCompra(dto))
                .isInstanceOf(SaldoInsuficienteException.class);

        // Verifica que o save da compra NUNCA foi chamado
        verify(compraRepository, never()).save(any(Compra.class));
    }

    @Test
    void deveLancarExcecaoQuandoJogoNaoEncontrado() {
        User usuario = criarUsuarioComSaldo(BigDecimal.valueOf(200.00));

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Mock que retorna vazio para a busca do jogo
        when(jogoRepository.findById(99L)).thenReturn(Optional.empty());

        CompraRequestDTO dto = criarDto(1L, 99L);

        // Verifica o lançamento de JogoNaoEncontradoException
        assertThatThrownBy(() -> compraService.realizarCompra(dto))
                .isInstanceOf(JogoNaoEncontradoException.class);
    }

    @Test
    void deveLancarExcecaoQuandoJogoJaAdquirido() {
        Jogo jogo = criarJogo(10L, BigDecimal.valueOf(59.90));

        // Usuário já possui o jogo na lista
        User usuario = criarUsuarioComSaldo(BigDecimal.valueOf(200.00));
        usuario.setJogos(List.of(jogo));

        // Mock informando que o usuário já possui aquele jogo
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(jogoRepository.findById(10L)).thenReturn(Optional.of(jogo));

        CompraRequestDTO dto = criarDto(1L, 10L);

        // Verifica o lançamento de JogoJaAdquiridoException
        assertThatThrownBy(() -> compraService.realizarCompra(dto))
                .isInstanceOf(JogoJaAdquiridoException.class);

        // O save da compra não deve ter sido chamado
        verify(compraRepository, never()).save(any(Compra.class));
    }
}
