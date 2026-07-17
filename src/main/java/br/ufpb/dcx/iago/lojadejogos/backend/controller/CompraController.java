package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelo processamento e histórico de compras.
 * Expõe os endpoints na rota base /api/v1/compras.
 */
@RestController
@RequestMapping("/api/v1/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    /**
     * Registra uma nova compra de jogo para um usuário na loja.
     * Rota: POST /api/v1/compras
     *
     * @param dto Objeto recebido no corpo da requisição (@RequestBody) com o ID do jogo e do usuário.
     * @return 201 Created contendo os dados e protocolo da compra (CompraResponseDTO).
     */
    @PostMapping
    public ResponseEntity<CompraResponseDTO> realizarCompra(@Valid @RequestBody CompraRequestDTO dto) {
        CompraResponseDTO compra = compraService.realizarCompra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compra);
    }


    /**
     * Lista todas as transações de compra realizadas globalmente na loja.
     * Rota: GET /api/v1/compras
     *
     * @return 200 OK acompanhado de uma lista de CompraResponseDTO contendo o histórico de compras.
     */
    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> listarTodasAsCompras() {
        List<CompraResponseDTO> compras = compraService.listarTodas();
        return ResponseEntity.ok(compras); // 200 OK
    }

    /**
     * Lista o histórico de compras de um usuário específico.
     * Rota: GET /api/v1/compras/usuario/{userId}
     *
     * @param userId Identificador do usuário fornecido na URL (@PathVariable).
     * @return 200 OK com uma lista de transações (CompraResponseDTO) feitas pelo usuário informado.
     */
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<CompraResponseDTO>> listarComprasPorUsuario(@PathVariable Long userId) {
        List<CompraResponseDTO> compras = compraService.listarComprasPorUsuario(userId);
        return ResponseEntity.ok(compras); // 200 OK
    }
}