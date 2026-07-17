package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.EmailJaCadastradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.UsuarioNaoEncontradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.model.Jogo;
import br.ufpb.dcx.iago.lojadejogos.backend.model.User;
import br.ufpb.dcx.iago.lojadejogos.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        return converterUserParaDTO(userSalvo);
    }

    public List<UserResponseDTO> listarTodos() {
        List<UserResponseDTO> list = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            list.add(converterUserParaDTO(user));
        }
        return list;
    }
    public UserResponseDTO buscarPorId(Long id) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        return converterUserParaDTO(usuario);
    }

    public UserResponseDTO atualizar(Long id, UserRequestDTO dto) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        User userAtualizado = userRepository.save(usuario);
        return converterUserParaDTO(userAtualizado);
    }

    public void deletarPorId(Long id){
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        userRepository.delete(usuario);
    }
    public List<JogoResponseDTO> listarJogosDOUsuario(Long id){
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não foi encontrado"));
        return usuario.getJogos().stream()
                .map(this:: converterJogoParaDTO)
                .toList();
    }

    private UserResponseDTO converterUserParaDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(user.getId());
        dto.setNome(user.getNome());
        dto.setSaldo(user.getSaldo());
        dto.setAdmin(user.isAdmin());
        // A mágica acontece aqui: não copiamos a senha para o DTO de resposta!
        return dto;
    }
    private JogoResponseDTO converterJogoParaDTO(Jogo jogo) {
        JogoResponseDTO dto = new JogoResponseDTO();

        dto.setId(jogo.getId());
        dto.setNome(jogo.getNome());
        dto.setPreco(jogo.getPreco());
        dto.setTipo(jogo.getTipo());
        dto.setUrlImagem(jogo.getUrlImagem());
        dto.setDescricao(jogo.getDescricao());

        return dto;
    }
}
