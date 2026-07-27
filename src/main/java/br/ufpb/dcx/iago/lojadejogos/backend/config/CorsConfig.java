package br.ufpb.dcx.iago.lojadejogos.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing).
 *
 * Define um CorsConfigurationSource como Bean para que TANTO o Spring MVC
 * QUANTO o Spring Security usem a mesma configuração.
 *
 * IMPORTANTE: Usar CorsConfigurationSource (em vez de WebMvcConfigurer) é
 * necessário para que o Spring Security processe corretamente o preflight
 * OPTIONS antes de aplicar as regras de autenticação.
 *
 * NOTA: allowCredentials(true) permite o envio do header "Authorization"
 * pelo frontend — NÃO pode ser usado junto com allowedOrigins("*").
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Origens permitidas — adicione aqui a porta do seu frontend
        // Em produção, troque por: "https://meusite.com"
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:4200",
                "http://127.0.0.1:4200",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:5173"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        config.setAllowedHeaders(List.of("*"));

        // Expor o header Authorization para que o frontend JavaScript consiga lê-lo
        config.setExposedHeaders(List.of("Authorization"));

        // Permite o envio de credenciais (Authorization header) pelo frontend
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
