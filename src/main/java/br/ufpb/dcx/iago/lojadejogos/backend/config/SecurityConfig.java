package br.ufpb.dcx.iago.lojadejogos.backend.config;

import br.ufpb.dcx.iago.lojadejogos.backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração central do Spring Security.
 *
 * Define:
 * - Quais rotas são públicas (login, registro, listagem de jogos)
 * - Quais rotas exigem autenticação (compras, dados do usuário)
 * - Quais rotas exigem role ADMIN (CRUD de jogos, listagem de usuários)
 * - Que a autenticação é STATELESS (sem sessão, via JWT)
 * - Que o JwtAuthenticationFilter roda ANTES do filtro padrão do Spring
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF — necessário para APIs REST stateless (sem cookies de sessão)
                .csrf(csrf -> csrf.disable())

                // Política STATELESS — o Spring NÃO cria sessão HTTP. Cada requisição
                // deve trazer o token JWT no header. Isso é o padrão correto para APIs REST.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Regras de autorização por rota
                .authorizeHttpRequests(auth -> auth
                        // --- ROTAS PÚBLICAS (qualquer pessoa acessa, sem token) ---
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()     // Registro
                        .requestMatchers(HttpMethod.GET, "/api/v1/jogos").permitAll()          // Vitrine
                        .requestMatchers(HttpMethod.GET, "/api/v1/jogos/**").permitAll()       // Detalhe

                        // --- ROTAS DE ADMIN (só quem tem ROLE_ADMIN) ---
                        .requestMatchers(HttpMethod.POST, "/api/v1/jogos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/jogos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/jogos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/compras").hasRole("ADMIN")

                        // --- TODAS AS OUTRAS ROTAS exigem estar autenticado ---
                        .anyRequest().authenticated()
                )

                // Registra o filtro JWT ANTES do filtro padrão de autenticação do Spring.
                // Assim, quando a requisição chega com um token válido, o Spring já sabe
                // quem é o usuário e qual a role dele ANTES de verificar as regras acima.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
