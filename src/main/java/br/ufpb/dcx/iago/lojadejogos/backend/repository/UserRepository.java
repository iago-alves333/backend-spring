package br.ufpb.dcx.iago.lojadejogos.backend.repository;

import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
