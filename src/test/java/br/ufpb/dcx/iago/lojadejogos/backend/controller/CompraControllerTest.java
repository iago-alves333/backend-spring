package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.SaldoInsuficienteException;
import br.ufpb.dcx.iago.lojadejogos.backend.service.CompraService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para o CompraController.
 * Valida os fluxos de realizar compra e tratamento de exceções.
 *
 * Utiliza Mockito puro sem servidor HTTP, injetando o mock do CompraService
 * diretamente no controlador para isolar a lógica do endpoint.
 */
@ExtendWith(MockitoExtension.class)
public class CompraControllerTest {

    /** Mock para CompraService — isola a camada de serviço */
    @Mock
    private CompraService compraService;

    /** Instância real do controller com mock injetado */
    @InjectMocks
    private CompraController compraController;

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private CompraResponseDTO criarCompraResponseDTO() {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setIdCompra(1L);
        dto.setNomeUsuario("Maria");
        dto.setNomeJogo("The Witcher 3");
        dto.setDataCompra(LocalDateTime.now());
        dto.setPreco(BigDecimal.valueOf(59.90));
        return dto;
    }

    private CompraRequestDTO criarCompraRequestDTO() {
        CompraRequestDTO dto = new CompraRequestDTO();
        dto.setUserId(1L);
        dto.setJogoId(10L);
        return dto;
    }

    // -------------------------------------------------------------------------
    // Testes
    // -------------------------------------------------------------------------

    @Test
    void testRealizarCompraComSucesso() {
        // Mock do serviço retornando a compra com sucesso
        when(compraService.realizarCompra(any(CompraRequestDTO.class)))
                .thenReturn(criarCompraResponseDTO());

        // Simula requisição POST para efetuar compra de um jogo
        ResponseEntity<CompraResponseDTO> resposta = compraController.realizarCompra(criarCompraRequestDTO());

        // Verifica o retorno da CompraResponseDTO com status 201 Created
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resposta.getBody()).isNotNull();
        assertThat(resposta.getBody().getNomeJogo()).isEqualTo("The Witcher 3");
        assertThat(resposta.getBody().getNomeUsuario()).isEqualTo("Maria");
        assertThat(resposta.getBody().getPreco()).isEqualByComparingTo(BigDecimal.valueOf(59.90));
    }

    @Test
    void testRealizarCompraSaldoInsuficiente() {
        // Simula cenário em que o usuário não tem saldo suficiente
        when(compraService.realizarCompra(any(CompraRequestDTO.class)))
                .thenThrow(new SaldoInsuficienteException("Saldo insuficiente para comprar esse jogo"));

        // Verifica se o controlador propaga a SaldoInsuficienteException
        // (o GlobalExceptionHandler captura e retorna 400 Bad Request em runtime)
        assertThatThrownBy(() -> compraController.realizarCompra(criarCompraRequestDTO()))
                .isInstanceOf(SaldoInsuficienteException.class)
                .hasMessageContaining("Saldo insuficiente");
    }

    @Test
    void testListarTodasAsCompras() {
        List<CompraResponseDTO> compras = List.of(criarCompraResponseDTO(), criarCompraResponseDTO());
        when(compraService.listarTodas()).thenReturn(compras);

        ResponseEntity<List<CompraResponseDTO>> resposta = compraController.listarTodasAsCompras();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).hasSize(2);
    }

    @Test
    void testListarComprasPorUsuario() {
        List<CompraResponseDTO> compras = List.of(criarCompraResponseDTO());
        when(compraService.listarComprasPorUsuario(1L)).thenReturn(compras);

        ResponseEntity<List<CompraResponseDTO>> resposta = compraController.listarComprasPorUsuario(1L);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).hasSize(1);
        assertThat(resposta.getBody().get(0).getNomeUsuario()).isEqualTo("Maria");
    }
}
