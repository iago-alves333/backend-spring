package br.ufpb.dcx.iago.lojadejogos.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing).
 *
 * ANTES: allowedOrigins("*") permitia que QUALQUER site fizesse requisições à API.
 * AGORA: Restringe para origens específicas do seu frontend.
 *
 * NOTA: Quando você usar JWT, o frontend precisa enviar o header "Authorization",
 * por isso adicionamos "Authorization" nos allowedHeaders e exposedHeaders.
 * O allowCredentials(true) é necessário quando o frontend envia cookies ou headers
 * de autenticação — mas NÃO pode ser usado junto com allowedOrigins("*").
 */
@Configuration
public class CorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // Restrinja aqui para os domínios do seu frontend.
                        // Em produção, troque por: "https://meusite.com"
                        .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        // Expor o header Authorization para que o frontend JavaScript consiga lê-lo
                        .exposedHeaders("Authorization")
                        // Permite o envio de credenciais (Authorization header) pelo frontend
                        .allowCredentials(true);
            }
        };
    }
}
