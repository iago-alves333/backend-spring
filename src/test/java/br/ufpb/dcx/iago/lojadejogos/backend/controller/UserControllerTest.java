package br.ufpb.dcx.iago.lojadejogos.backend.controller;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserRequestDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.UserResponseDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.EmailJaCadastradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.exception.UsuarioNaoEncontradoException;
import br.ufpb.dcx.iago.lojadejogos.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequestDTO criarUserRequestDTO(String email) {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNome("User Teste");
        dto.setEmail(email);
        dto.setSenha("senha123");
        return dto;
    }

    @Test
    void deveLancarExcecaoAoSalvarUsuarioComEmailDuplicado() {
        UserRequestDTO dto = criarUserRequestDTO("duplicado@teste.com");

        when(userService.salvar(any(UserRequestDTO.class)))
                .thenThrow(new EmailJaCadastradoException("duplicado@teste.com"));

        assertThatThrownBy(() -> userController.salvarUsuario(dto))
                .isInstanceOf(EmailJaCadastradoException.class);
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(userService.buscarPorId(99L))
                .thenThrow(new UsuarioNaoEncontradoException("Usuário não encontrado"));

        assertThatThrownBy(() -> userController.buscarUsuarioPorId(99L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioComEmailDuplicado() {
        UserRequestDTO dto = criarUserRequestDTO("duplicado@teste.com");

        when(userService.atualizar(eq(1L), any(UserRequestDTO.class)))
                .thenThrow(new EmailJaCadastradoException("duplicado@teste.com"));

        assertThatThrownBy(() -> userController.atualizarUsuario(1L, dto))
                .isInstanceOf(EmailJaCadastradoException.class);
    }
}
