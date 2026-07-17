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

/**
 * Serviço responsável pela lógica de negócio envolvendo usuários.
 * Gerencia operações de CRUD, validações de integridade e regras específicas,
 * como garantir a unicidade do e-mail.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cadastra um novo usuário no sistema.
     * Valida se o email já está cadastrado antes de prosseguir.
     * Por padrão, define o nível de acesso (admin) como falso e criptografa a senha.
     *
     * @param dto Os dados do usuário a ser cadastrado.
     * @return UserResponseDTO com os dados salvos, omitindo dados sensíveis como senha.
     * @throws EmailJaCadastradoException Se o email já existir na base de dados.
     */
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
    /**
     * Busca os dados de um usuário através do ID fornecido.
     *
     * @param id Identificador do usuário.
     * @return UserResponseDTO contendo as informações do usuário.
     * @throws UsuarioNaoEncontradoException Caso o ID não corresponda a nenhum usuário existente.
     */
    public UserResponseDTO buscarPorId(Long id) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        return converterUserParaDTO(usuario);
    }

    /**
     * Atualiza os dados básicos (nome e e-mail) de um usuário existente.
     * 
     * @param id Identificador do usuário a ser atualizado.
     * @param dto Novos dados para o usuário.
     * @return UserResponseDTO com os dados atualizados.
     * @throws UsuarioNaoEncontradoException Se o usuário não existir.
     */
    public UserResponseDTO atualizar(Long id, UserRequestDTO dto) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        User userAtualizado = userRepository.save(usuario);
        return converterUserParaDTO(userAtualizado);
    }

    /**
     * Remove um usuário permanentemente do sistema pelo seu ID.
     *
     * @param id Identificador do usuário a ser removido.
     * @throws UsuarioNaoEncontradoException Se o usuário não existir.
     */
    public void deletarPorId(Long id){
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        userRepository.delete(usuario);
    }
    /**
     * Retorna a biblioteca de jogos adquiridos por um usuário específico.
     *
     * @param id Identificador do usuário.
     * @return Uma lista de JogoResponseDTO contendo os detalhes dos jogos do usuário.
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado.
     */
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
