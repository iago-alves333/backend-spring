package br.ufpb.dcx.iago.lojadejogos.backend.repository;

import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Compra;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra,Long> {
    List<Compra> findByUserId(Long userId);

}
