package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.ResourceNotFoundException;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JogoService {

    @Autowired
    private JogoRepository jogoRepository;

    public List<JogoResponseDTO> listarTodos() {
        List<JogoResponseDTO> novaLista = new ArrayList<>();
        for (Jogo j : jogoRepository.findAll()) {
            novaLista.add(converterParaDTO(j));
        }
        return novaLista;
    }

    public JogoResponseDTO salvar(JogoRequestDTO dto) {
        Jogo jogo = new Jogo();
        jogo.setNome(dto.getNome());
        jogo.setPreco(dto.getPreco());
        jogo.setTipo(dto.getTipo());
        jogo.setUrlImagem(dto.getUrlImagem());

        Jogo jogoSalvo = jogoRepository.save(jogo);

        return converterParaDTO(jogoSalvo);
    }

    public JogoResponseDTO buscarPorId(Long id) throws ResourceNotFoundException {
        // Correção 1.2: Uso correto do orElseThrow com lambda
        Jogo j = jogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        return converterParaDTO(j);
    }

    public void deletarPorId(Long id) throws ResourceNotFoundException {
        // Correção 1.4: Verifica com existsById e lança ResourceNotFoundException
        if (!jogoRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        jogoRepository.deleteById(id);
    }

    public JogoResponseDTO atualizar(Long id, JogoRequestDTO dto) throws ResourceNotFoundException {
        // Correção 1.3: Padronizado usando orElseThrow, código mais limpo
        Jogo jogoExistente = jogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        jogoExistente.setNome(dto.getNome());
        jogoExistente.setPreco(dto.getPreco());
        jogoExistente.setTipo(dto.getTipo());
        jogoExistente.setUrlImagem(dto.getUrlImagem());

        Jogo jogoAtualizado = jogoRepository.save(jogoExistente);

        return converterParaDTO(jogoAtualizado);
    }

    private JogoResponseDTO converterParaDTO(Jogo jogo) {
        JogoResponseDTO dto = new JogoResponseDTO();
        dto.setId(jogo.getId());
        dto.setNome(jogo.getNome());
        dto.setPreco(jogo.getPreco());
        dto.setTipo(jogo.getTipo());
        dto.setUrlImagem(jogo.getUrlImagem());
        return dto;
    }
}