package br.ufpb.dcx.iago.lojadejogos.backend.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("O recurso solicitado não foi encontrado.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}