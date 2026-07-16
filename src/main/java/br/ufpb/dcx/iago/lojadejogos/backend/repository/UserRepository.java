package br.ufpb.dcx.iago.lojadejogos.backend.repository;

import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Busca usuário por email — usado pelo Spring Security para autenticação
    Optional<User> findByEmail(String email);
}
