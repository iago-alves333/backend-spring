package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus; // Não esqueça deste import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> salvarUsuario(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO usuarioSalvo = userService.salvar(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> usuarios = userService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
        UserResponseDTO usuario = userService.buscarPorId(id);
        return ResponseEntity.ok(usuario); // 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO usuarioAtualizado = userService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioAtualizado); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        userService.deletarPorId(id);
        return ResponseEntity.noContent().build(); // 204 NO CONTENT
    }

}