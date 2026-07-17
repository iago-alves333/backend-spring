package br.ufpb.dcx.iago.lojadejogos.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Classe principal de inicialização da aplicação Spring Boot.
 * Ponto de entrada do sistema que configura o contexto e sobe o servidor embutido.
 */
public class LojaDeJogosApplication {

    public static void main(String[] args) {
        SpringApplication.run(LojaDeJogosApplication.class, args);
    }
}
