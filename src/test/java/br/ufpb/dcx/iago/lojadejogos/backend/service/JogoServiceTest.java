package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.PrecoInvalidoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.ResourceNotFoundException;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.JogoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JogoServiceTest {

    @Mock
    private JogoRepository jogoRepository;

    @InjectMocks
    private JogoService jogoService;

    private JogoRequestDTO criarJogoRequestDTO(BigDecimal preco) {
        JogoRequestDTO dto = new JogoRequestDTO();
        dto.setNome("Cyberpunk 2077");
        dto.setPreco(preco);
        dto.setTipo("Ação");
        dto.setDescricao("Um jogo futurista");
        dto.setUrlImagem("url");
        return dto;
    }

    private Jogo criarJogoSalvo(BigDecimal preco) {
        Jogo jogo = new Jogo();
        jogo.setId(1L);
        jogo.setNome("Cyberpunk 2077");
        jogo.setPreco(preco);
        jogo.setTipo("Ação");
        jogo.setDescricao("Um jogo futurista");
        jogo.setUrlImagem("url");
        return jogo;
    }

    @Test
    void deveLancarExcecaoAoSalvarJogoComPrecoNegativo() {
        JogoRequestDTO dto = criarJogoRequestDTO(BigDecimal.valueOf(-10.0));

        assertThatThrownBy(() -> jogoService.salvar(dto))
                .isInstanceOf(PrecoInvalidoException.class)
                .hasMessageContaining("negativo");

        verify(jogoRepository, never()).save(any(Jogo.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarJogoComPrecoNegativo() {
        JogoRequestDTO dto = criarJogoRequestDTO(BigDecimal.valueOf(-15.0));

        assertThatThrownBy(() -> jogoService.atualizar(1L, dto))
                .isInstanceOf(PrecoInvalidoException.class)
                .hasMessageContaining("negativo");

        verify(jogoRepository, never()).save(any(Jogo.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarJogoInexistente() {
        when(jogoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jogoService.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deveLancarExcecaoAoDeletarJogoInexistente() {
        when(jogoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> jogoService.deletarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deveSalvarJogoComSucesso() {
        JogoRequestDTO dto = criarJogoRequestDTO(BigDecimal.valueOf(100.0));
        Jogo jogoSalvo = criarJogoSalvo(BigDecimal.valueOf(100.0));

        when(jogoRepository.save(any(Jogo.class))).thenReturn(jogoSalvo);

        JogoResponseDTO resposta = jogoService.salvar(dto);

        assertThat(resposta.getNome()).isEqualTo("Cyberpunk 2077");
        assertThat(resposta.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(100.0));
        verify(jogoRepository, times(1)).save(any(Jogo.class));
    }

    @Test
    void deveListarTodosOsJogos() {
        Jogo jogo = criarJogoSalvo(BigDecimal.valueOf(100.0));
        when(jogoRepository.findAll()).thenReturn(java.util.List.of(jogo));

        java.util.List<JogoResponseDTO> resposta = jogoService.listarTodos();

        assertThat(resposta).hasSize(1);
        assertThat(resposta.get(0).getNome()).isEqualTo("Cyberpunk 2077");
        verify(jogoRepository, times(1)).findAll();
    }

    @Test
    void deveAtualizarJogoComSucesso() {
        Jogo jogoExistente = criarJogoSalvo(BigDecimal.valueOf(100.0));
        JogoRequestDTO dto = criarJogoRequestDTO(BigDecimal.valueOf(150.0));

        when(jogoRepository.findById(1L)).thenReturn(Optional.of(jogoExistente));
        when(jogoRepository.save(any(Jogo.class))).thenReturn(jogoExistente);

        JogoResponseDTO resposta = jogoService.atualizar(1L, dto);

        assertThat(resposta.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(150.0));
        verify(jogoRepository, times(1)).save(any(Jogo.class));
    }
}

