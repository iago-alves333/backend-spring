package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.PrecoInvalidoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.ResourceNotFoundException;
import br.ufpb.dcx.iago.lojadejogos.backend.service.JogoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JogoControllerTest {

    @Mock
    private JogoService jogoService;

    @InjectMocks
    private JogoController jogoController;

    private JogoRequestDTO criarJogoRequestDTO(BigDecimal preco) {
        JogoRequestDTO dto = new JogoRequestDTO();
        dto.setNome("Jogo Teste");
        dto.setPreco(preco);
        dto.setTipo("Aventura");
        dto.setDescricao("Desc");
        dto.setUrlImagem("url");
        return dto;
    }

    @Test
    void deveLancarExcecaoAoSalvarJogoComPrecoNegativo() {
        JogoRequestDTO dto = criarJogoRequestDTO(BigDecimal.valueOf(-10.0));

        when(jogoService.salvar(any(JogoRequestDTO.class)))
                .thenThrow(new PrecoInvalidoException("O preço do jogo não pode ser negativo."));

        assertThatThrownBy(() -> jogoController.salvarJogo(dto))
                .isInstanceOf(PrecoInvalidoException.class);
    }

    @Test
    void deveLancarExcecaoAoBuscarJogoInexistente() {
        when(jogoService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException());

        assertThatThrownBy(() -> jogoController.buscarJogoPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deveLancarExcecaoAoAtualizarJogoComPrecoNegativo() {
        JogoRequestDTO dto = criarJogoRequestDTO(BigDecimal.valueOf(-5.0));

        when(jogoService.atualizar(eq(1L), any(JogoRequestDTO.class)))
                .thenThrow(new PrecoInvalidoException("O preço do jogo não pode ser negativo."));

        assertThatThrownBy(() -> jogoController.atualizar(1L, dto))
                .isInstanceOf(PrecoInvalidoException.class);
    }

    @Test
    void deveLancarExcecaoAoDeletarJogoInexistente() {
        doThrow(new ResourceNotFoundException()).when(jogoService).deletarPorId(99L);

        assertThatThrownBy(() -> jogoController.removerJogoPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
