package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.CompraResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.JogoJaAdquiridoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.JogoNaoEncontradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.SaldoInsuficienteException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.UsuarioNaoEncontradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Compra;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.CompraRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.JogoRepository;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {


    private final CompraRepository compraRepository;

    private final JogoRepository jogoRepository;

    private final UserRepository userRepository;

    public CompraService(CompraRepository compraRepository, JogoRepository jogoRepository,  UserRepository userRepository) {
        this.compraRepository = compraRepository;
        this.jogoRepository = jogoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CompraResponseDTO realizarCompra(CompraRequestDTO dto) {

        User usuario = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));

        Jogo jogo = jogoRepository.findById(dto.getJogoId())
                .orElseThrow(() -> new JogoNaoEncontradoException("Jogo não encontrado!"));
        boolean jaPossuiJogo = usuario.getJogos().stream()
                .anyMatch(j -> j.getId().equals(jogo.getId()));
        if (jaPossuiJogo) {
            throw new JogoJaAdquiridoException("Você já possui este jogo!");        }

        if(usuario.getSaldo().compareTo(jogo.getPreco()) < 0){
            throw new SaldoInsuficienteException("Saldo insufiencete para comprar esse jogo");
        }

        usuario.setSaldo(usuario.getSaldo().subtract(jogo.getPreco()));
        usuario.getJogos().add(jogo);
        userRepository.save(usuario);

        Compra novaCompra = new Compra();
        novaCompra.setUser(usuario);
        novaCompra.setJogo(jogo);
        novaCompra.setDataCompra(LocalDateTime.now());
        novaCompra.setValorPago(jogo.getPreco());



        Compra compraSalva = compraRepository.save(novaCompra);

        return converterParaDTO(compraSalva);
    }

    public List<CompraResponseDTO> listarTodas(){
        return compraRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<CompraResponseDTO> listarComprasPorUsuario(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado");
        }
        return compraRepository.findById(userId).stream()
                .map(this::converterParaDTO)
                .toList();
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