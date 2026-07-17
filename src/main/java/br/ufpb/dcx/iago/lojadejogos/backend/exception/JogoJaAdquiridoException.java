package br.ufpb.dcx.iago.lojadejogos.backend.exception;

/**
 * Lançada quando o usuário tenta comprar um jogo que já possui na biblioteca.
 * Devolve status HTTP 409 (Conflict).
 */
public class JogoJaAdquiridoException extends RuntimeException {
    public JogoJaAdquiridoException(String message) {
        super(message);
    }
}