package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.JogoRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.service.JogoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    @Autowired
    private JogoService jogoService;

    @GetMapping
    public List<JogoResponseDTO> listarTodosOsJogos() {
        return jogoService.listarTodos();
    }

    @PostMapping
    public JogoResponseDTO salvarJogo(@Valid @RequestBody JogoRequestDTO dto) {
        return jogoService.salvar(dto);
    }

    @GetMapping("/{id}")
    public JogoResponseDTO buscarJogoPorId(@PathVariable Long id) {
        return jogoService.buscarPorId(id);
    }
    @DeleteMapping("/{id}")
    public void removerJogoPorId(@PathVariable Long id) {
        jogoService.deletarPorId(id);
    }
    @PutMapping("/{id}")
    public JogoResponseDTO atualizar(@PathVariable Long id,@Valid @RequestBody JogoRequestDTO dto) {
        return jogoService.atualizar(id, dto);
    }
}
