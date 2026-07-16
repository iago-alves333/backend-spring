package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.EmailJaCadastradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO salvar(UserRequestDTO dto) {
        // Verifica se já existe um usuário com esse email ANTES de tentar salvar.
        // Sem essa verificação, o erro só apareceria como uma exceção genérica do banco
        // (DataIntegrityViolationException), sem mensagem amigável para o cliente.
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setSaldo(dto.getSaldo());
        // isAdmin é SEMPRE false no registro. Apenas um Admin pode promover outro usuário.
        user.setAdmin(false);

        User userSalvo = userRepository.save(user);

        return converterParaDTO(userSalvo);
    }

    public List<UserResponseDTO> listarTodos() {
        List<UserResponseDTO> list = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            list.add(converterParaDTO(user));
        }
        return list;
    }

    private UserResponseDTO converterParaDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(user.getId());
        dto.setNome(user.getNome());
        dto.setSaldo(user.getSaldo());
        dto.setAdmin(user.isAdmin());
        // A mágica acontece aqui: não copiamos a senha para o DTO de resposta!
        return dto;
    }
}
