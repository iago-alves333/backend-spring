package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.service.JogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciar operações relacionadas ao catálogo de jogos.
 * Expõe os endpoints na rota base /api/v1/jogos.
 */
@RestController
@RequestMapping("/api/v1/jogos")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    /**
     * Retorna a lista completa de jogos disponíveis na loja.
     * Rota: GET /api/v1/jogos
     *
     * @return 200 OK com uma lista de JogoResponseDTO no corpo da resposta.
     */
    @GetMapping
    public ResponseEntity<List<JogoResponseDTO>> listarTodosOsJogos() {
        List<JogoResponseDTO> jogos = jogoService.listarTodos();
        return ResponseEntity.ok(jogos);
    }

    /**
     * Adiciona um novo jogo ao catálogo da loja.
     * Rota: POST /api/v1/jogos
     *
     * @param dto Corpo da requisição (@RequestBody) contendo os dados de criação do jogo.
     * @return 201 Created contendo os dados do jogo recém-salvo no formato JogoResponseDTO.
     */
    @PostMapping
    public ResponseEntity<JogoResponseDTO> salvarJogo(@Valid @RequestBody JogoRequestDTO dto) {
        JogoResponseDTO jogoSalvo = jogoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoSalvo);
    }

    /**
     * Busca os detalhes de um jogo específico através do seu ID.
     * Rota: GET /api/v1/jogos/{id}
     *
     * @param id Parâmetro de rota (@PathVariable) com o identificador numérico do jogo.
     * @return 200 OK com os dados do jogo, ou erro caso não exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> buscarJogoPorId(@PathVariable Long id) {
        JogoResponseDTO jogo = jogoService.buscarPorId(id);
        return ResponseEntity.ok(jogo);
    }

    /**
     * Remove um jogo do catálogo permanentemente.
     * Rota: DELETE /api/v1/jogos/{id}
     *
     * @param id Parâmetro de rota (@PathVariable) com o identificador do jogo a ser removido.
     * @return 204 No Content para confirmar a deleção bem-sucedida, sem corpo na resposta.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerJogoPorId(@PathVariable Long id) {
        jogoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza os dados de um jogo existente no catálogo.
     * Rota: PUT /api/v1/jogos/{id}
     *
     * @param id Identificador do jogo passado na rota (@PathVariable).
     * @param dto Novos dados para o jogo enviados no corpo da requisição (@RequestBody).
     * @return 200 OK com os dados atualizados do jogo em formato JogoResponseDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JogoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody JogoRequestDTO dto) {
        JogoResponseDTO jogoAtualizado = jogoService.atualizar(id, dto);
        return ResponseEntity.ok(jogoAtualizado);
    }
}