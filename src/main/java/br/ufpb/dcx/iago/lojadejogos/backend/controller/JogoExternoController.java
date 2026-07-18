package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoBuscaExternaDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.service.IgdbIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/jogos/externo")
public class JogoExternoController {
    private final IgdbIntegrationService igdbService;

    public JogoExternoController(IgdbIntegrationService igdbService) {
        this.igdbService = igdbService;
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<JogoBuscaExternaDTO>> buscarNaIgdb(@RequestParam String nome){
        List<JogoBuscaExternaDTO> resultados = igdbService.buscarJogosPorNome(nome);
        return ResponseEntity.ok(resultados);
    }

}
