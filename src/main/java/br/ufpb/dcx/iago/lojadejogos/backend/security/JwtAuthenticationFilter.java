package br.ufpb.dcx.iago.lojadejogos.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que intercepta TODAS as requisições HTTP antes de chegarem aos Controllers.
 * Ele extrai o token JWT do header "Authorization", valida, e configura a autenticação
 * no SecurityContext do Spring Security.
 *
 * Fluxo:
 * 1. Requisição chega → filtro verifica se tem header "Authorization: Bearer <token>"
 * 2. Se não tem → passa adiante sem autenticar (as rotas públicas funcionam normalmente)
 * 3. Se tem → valida o token, extrai email e role, e configura o SecurityContext
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extrair o header "Authorization"
        String authHeader = request.getHeader("Authorization");

        // 2. Se não existe ou não começa com "Bearer ", passa adiante sem autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrair o token (remover o prefixo "Bearer ")
        String token = authHeader.substring(7);

        // 4. Validar o token
        if (jwtService.isTokenValido(token)) {
            String email = jwtService.extrairEmail(token);
            String role = jwtService.extrairRole(token);

            // 5. Criar a autenticação com a role do usuário (ROLE_USER ou ROLE_ADMIN)
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

            // 6. Colocar no SecurityContext — a partir daqui, o Spring sabe quem está autenticado
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. Continuar a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
