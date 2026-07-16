package br.ufpb.dcx.iago.lojadejogos.backend.exception;

/**
 * Exceção lançada quando se tenta cadastrar um usuário com um email que já existe no banco.
 * É tratada pelo GlobalExceptionHandler para retornar HTTP 409 (Conflict) com mensagem amigável.
 */
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("Já existe um usuário cadastrado com o email: " + email);
    }
}
