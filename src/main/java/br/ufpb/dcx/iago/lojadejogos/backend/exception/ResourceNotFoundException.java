package br.ufpb.dcx.iago.lojadejogos.backend.exception;

/**
 * Exceção genérica lançada quando um recurso solicitado (usuário, jogo, compra) não existe no banco.
 * Devolve status HTTP 404 (Not Found).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("O recurso solicitado não foi encontrado.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}