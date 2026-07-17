package br.ufpb.dcx.iago.lojadejogos.backend.repository;

import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// TODO: Fazer a interface estender JpaRepository<Jogo, TipoDoId>
/**
 * Repositório para operações de persistência da entidade Jogo.
 * Estende JpaRepository fornecendo acesso a dados e consultas encapsuladas.
 */
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    // TODO: Adicionar métodos de busca personalizados, caso necessário (ex: findByNome)

}
