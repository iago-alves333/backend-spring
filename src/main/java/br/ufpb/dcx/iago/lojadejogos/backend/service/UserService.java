package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO salvar(UserRequestDTO dto) {
        User user = new User();
        user.setNome(dto.getNome());
        user.setSenha(dto.getSenha());
        user.setSaldo(dto.getSaldo());
        user.setAdmin(dto.isAdmin());

        User userSalvo = userRepository.save(user);

        return  converterParaDTO(userSalvo);
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
