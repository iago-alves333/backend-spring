package br.ufpb.dcx.iago.lojadejogos.backend.repository;

import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// TODO: Fazer a interface estender JpaRepository<Jogo, TipoDoId>
/**
 * Repositório para operações de persistência da entidade Jogo.
 * Estende JpaRepository fornecendo acesso a dados e consultas encapsuladas.
 */
public interface JogoRepository extends JpaRepository<Jogo, Long> {

    @Modifying
    @Query(value = "DELETE FROM usuario_jogos WHERE jogo_id = :jogoId", nativeQuery = true)
    void removerAssociacoesUsuarioJogo(@Param("jogoId") Long jogoId);
}
