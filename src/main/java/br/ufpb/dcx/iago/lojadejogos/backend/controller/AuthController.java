package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.LoginRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.LoginResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação.
 * Rota pública: qualquer pessoa pode fazer POST /auth/login para obter um token JWT.
 * Rota protegida: GET /auth/me retorna os dados do usuário autenticado.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint para autenticação de usuários na plataforma.
     * Rota: POST /api/v1/auth/login
     * Recebe email e senha, valida contra o banco, e retorna um token JWT.
     *
     * @param dto Corpo da requisição (@RequestBody) contendo as credenciais de login.
     * @return 200 OK com LoginResponseDTO contendo o token JWT se as credenciais forem válidas, ou 401 Unauthorized em caso de falha.
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

    /**
     * Retorna os dados do usuário atualmente autenticado.
     * Rota: GET /api/v1/auth/me
     *
     * O email do usuário é extraído do SecurityContext, que foi populado
     * pelo {@link br.ufpb.dcx.iago.lojadejogos.backend.security.JwtAuthenticationFilter}
     * ao validar o token JWT enviado no header Authorization.
     *
     * @return 200 OK com UserResponseDTO contendo id, nome, saldo e isAdmin.
     *         401 Unauthorized se o token for inválido ou ausente.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> obterUsuarioAutenticado() {
        // O JwtAuthenticationFilter já populou o SecurityContext com o email como principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Busca o usuário completo no banco pelo email extraído do token
        User usuario = userRepository.findByEmail(email)
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Converte para DTO (sem dados sensíveis como senha)
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSaldo(usuario.getSaldo());
        dto.setAdmin(usuario.isAdmin());

        return ResponseEntity.ok(dto);
    }
}
