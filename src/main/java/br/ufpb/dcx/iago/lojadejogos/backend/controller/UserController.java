package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus; // Não esqueça deste import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para o gerenciamento de usuários do sistema.
 * Expõe os endpoints na rota base /api/v1/usuarios.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Cadastra um novo usuário no sistema.
     * Rota: POST /api/v1/usuarios
     *
     * @param userRequestDTO Dados enviados no corpo da requisição (@RequestBody) para criar a conta.
     * @return 201 Created com os dados não sensíveis do usuário recém-criado.
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> salvarUsuario(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO usuarioSalvo = userService.salvar(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    /**
     * Lista todos os usuários cadastrados no banco de dados.
     * Rota: GET /api/v1/usuarios
     *
     * @return 200 OK com a lista de UserResponseDTO.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> usuarios = userService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca os dados detalhados de um usuário específico pelo seu ID.
     * Rota: GET /api/v1/usuarios/{id}
     *
     * @param id Parâmetro da URL (@PathVariable) correspondente ao ID do usuário.
     * @return 200 OK contendo os dados do usuário encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
        UserResponseDTO usuario = userService.buscarPorId(id);
        return ResponseEntity.ok(usuario); // 200 OK
    }

    /**
     * Atualiza o nome ou e-mail de um usuário existente.
     * Rota: PUT /api/v1/usuarios/{id}
     *
     * @param id ID do usuário passado como parâmetro da rota (@PathVariable).
     * @param dto Corpo da requisição (@RequestBody) contendo os novos dados do usuário.
     * @return 200 OK com as informações atualizadas do usuário.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO usuarioAtualizado = userService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioAtualizado); // 200 OK
    }

    /**
     * Adiciona saldo à carteira de um usuário.
     * Rota: PATCH ou POST /api/v1/usuarios/{id}/saldo
     *
     * @param id ID do usuário passado como parâmetro da rota (@PathVariable).
     * @param payload Map contendo a chave "valor" com o montante a ser adicionado.
     * @return 200 OK com as informações atualizadas do usuário.
     */
    @RequestMapping(value = "/{id}/saldo", method = {RequestMethod.PATCH, RequestMethod.POST})
    public ResponseEntity<UserResponseDTO> adicionarSaldo(@PathVariable Long id, @RequestBody Map<String, BigDecimal> payload) {
        BigDecimal valor = payload.get("valor");
        UserResponseDTO usuarioAtualizado = userService.adicionarSaldo(id, valor);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    /**
     * Remove permanentemente a conta de um usuário do sistema.
     * Rota: DELETE /api/v1/usuarios/{id}
     *
     * @param id Parâmetro de rota (@PathVariable) indicando qual usuário deletar.
     * @return 204 No Content confirmando a exclusão.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        userService.deletarPorId(id);
        return ResponseEntity.noContent().build(); // 204 NO CONTENT
    }

    /**
     * Retorna a lista de jogos adquiridos e presentes na biblioteca de um usuário específico.
     * Rota: GET /api/v1/usuarios/{id}/jogos
     *
     * @param id Parâmetro da URL (@PathVariable) com o ID do usuário.
     * @return 200 OK com uma lista de JogoResponseDTO representando a biblioteca do usuário.
     */
    @GetMapping("/{id}/jogos")
    public ResponseEntity<List<JogoResponseDTO>> listarJogosDoUsuario(@PathVariable Long id) {
        List<JogoResponseDTO> jogos = userService.listarJogosDOUsuario(id);
        return ResponseEntity.ok(jogos);
    }
}