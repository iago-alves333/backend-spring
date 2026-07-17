package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> realizarCompra(@Valid @RequestBody CompraRequestDTO dto) {
        CompraResponseDTO compra = compraService.realizarCompra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compra);
    }


    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> listarTodasAsCompras() {
        List<CompraResponseDTO> compras = compraService.listarTodas();
        return ResponseEntity.ok(compras); // 200 OK
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<CompraResponseDTO>> listarComprasPorUsuario(@PathVariable Long userId) {
        List<CompraResponseDTO> compras = compraService.listarComprasPorUsuario(userId);
        return ResponseEntity.ok(compras); // 200 OK
    }
}