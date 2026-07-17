package br.ufpb.dcx.iago.lojadejogos.backend.exception;

/**
 * Lançada quando um usuário específico não é encontrado no banco de dados.
 * Devolve status HTTP 404 (Not Found).
 */
public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}