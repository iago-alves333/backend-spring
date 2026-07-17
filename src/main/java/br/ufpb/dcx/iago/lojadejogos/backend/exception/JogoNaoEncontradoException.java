package br.ufpb.dcx.iago.lojadejogos.backend.exception;

/**
 * Lançada quando uma operação tenta buscar ou interagir com um jogo inexistente.
 * Devolve status HTTP 404 (Not Found).
 */
public class JogoNaoEncontradoException extends RuntimeException {
    public JogoNaoEncontradoException(String message) {
        super(message);
    }
}