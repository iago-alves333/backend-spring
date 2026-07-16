package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Compra;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.CompraRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.JogoRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private UserRepository userRepository;

    public CompraResponseDTO realizarCompra(CompraRequestDTO dto) {

        User usuario = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Jogo jogo = jogoRepository.findById(dto.getJogoId())
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado!"));

        Compra novaCompra = new Compra();
        novaCompra.setUser(usuario);
        novaCompra.setJogo(jogo);
        novaCompra.setDataCompra(LocalDateTime.now());
        novaCompra.setValorPago(jogo.getPreco());

        Compra compraSalva = compraRepository.save(novaCompra);

        return converterParaDTO(compraSalva);
    }

    // --- MÉTODO AUXILIAR DE CONVERSÃO ---
    private CompraResponseDTO converterParaDTO(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setIdCompra(compra.getId());
        dto.setNomeUsuario(compra.getUser().getNome());
        dto.setNomeJogo(compra.getJogo().getNome());
        dto.setDataCompra(compra.getDataCompra());
        dto.setPreco(compra.getValorPago());

        return dto;
    }
}