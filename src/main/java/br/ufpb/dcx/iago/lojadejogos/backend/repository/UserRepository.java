package br.ufpb.dcx.iago.lojadejogos.backend.repository;

import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para operações de persistência da entidade User.
 * Estende JpaRepository para prover métodos CRUD padrão e consultas customizadas.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // Busca usuário por email — usado pelo Spring Security para autenticação
    Optional<User> findByEmail(String email);
}
