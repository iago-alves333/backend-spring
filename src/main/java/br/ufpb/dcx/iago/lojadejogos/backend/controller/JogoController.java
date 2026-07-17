package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.service.JogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @GetMapping
    public ResponseEntity<List<JogoResponseDTO>> listarTodosOsJogos() {
        List<JogoResponseDTO> jogos = jogoService.listarTodos();
        return ResponseEntity.ok(jogos);
    }

    @PostMapping
    public ResponseEntity<JogoResponseDTO> salvarJogo(@Valid @RequestBody JogoRequestDTO dto) {
        JogoResponseDTO jogoSalvo = jogoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> buscarJogoPorId(@PathVariable Long id) {
        JogoResponseDTO jogo = jogoService.buscarPorId(id);
        return ResponseEntity.ok(jogo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerJogoPorId(@PathVariable Long id) {
        jogoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody JogoRequestDTO dto) {
        JogoResponseDTO jogoAtualizado = jogoService.atualizar(id, dto);
        return ResponseEntity.ok(jogoAtualizado);
    }
}