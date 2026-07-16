package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.JogoRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.service.JogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jogo")
// TODO: Adicionar anotação @RestController e @RequestMapping para definir a rota base (ex: /jogos)
public class JogoController {
    // TODO: Injetar o JogoService
    // TODO: Criar os métodos (endpoints) anotados com @GetMapping, @PostMapping, @PutMapping, @DeleteMapping

    @Autowired
    private JogoService jogoService;

    @GetMapping
    public List<JogoResponseDTO> listarTodosOsJogos() {
        return jogoService.listarTodos();
    }

    @PostMapping
    public JogoResponseDTO salvarJogo(@RequestBody JogoRequestDTO dto) {
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
    public JogoResponseDTO atualizar(@PathVariable Long id, @RequestBody JogoRequestDTO dto) {
        return jogoService.atualizar(id, dto);
    }
}
