package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.LoginRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.LoginResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de autenticação.
 * Rota pública: qualquer pessoa pode fazer POST /auth/login para obter um token JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint de login.
     * Recebe email + senha, valida contra o banco, e retorna um token JWT.
     *
     * Fluxo:
     * 1. Busca o usuário pelo email
     * 2. Compara a senha enviada com o hash BCrypt armazenado no banco
     * 3. Se válido, gera e retorna o token JWT com a role do usuário
     * 4. Se inválido, retorna 401 Unauthorized
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        // Busca o usuário pelo email no banco
        User usuario = userRepository.findByEmail(dto.getEmail())
                .orElse(null);

        // Verifica se o usuário existe E se a senha bate com o hash BCrypt
        if (usuario == null || !passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
        }

        // Define a role baseada no campo isAdmin do usuário
        String role = usuario.isAdmin() ? "ADMIN" : "USER";

        // Gera o token JWT
        String token = jwtService.gerarToken(usuario.getEmail(), role);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
