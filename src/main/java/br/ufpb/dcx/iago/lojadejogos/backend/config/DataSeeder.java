package br.ufpb.dcx.iago.lojadejogos.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Executa o script data.sql após o Hibernate criar/atualizar as tabelas.
 *
 * Por que usar ApplicationRunner em vez de spring.sql.init.mode=always?
 * No Spring Boot 4.x com JPA, o inicializador SQL padrão roda ANTES do Hibernate
 * criar as tabelas, causando erros de "tabela não existe". O ApplicationRunner
 * roda depois que todo o contexto Spring (incluindo JPA/Hibernate) está pronto.
 *
 * O script usa ON CONFLICT DO NOTHING, então é seguro rodar múltiplas vezes.
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final DataSource dataSource;

    public DataSeeder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassPathResource script = new ClassPathResource("data.sql");

        if (!script.exists()) {
            log.info("data.sql não encontrado em resources — pulando seed.");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, script);
            log.info("✅ data.sql executado com sucesso — banco populado.");
        } catch (Exception e) {
            log.error("❌ Erro ao executar data.sql: {}", e.getMessage());
            // Não propaga a exceção para não derrubar a aplicação
            // se o banco já tiver dados (conflito de constraint)
        }
    }
}
