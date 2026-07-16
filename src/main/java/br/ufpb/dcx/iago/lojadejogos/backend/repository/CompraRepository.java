package br.ufpb.dcx.iago.lojadejogos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra,Long> {

}
