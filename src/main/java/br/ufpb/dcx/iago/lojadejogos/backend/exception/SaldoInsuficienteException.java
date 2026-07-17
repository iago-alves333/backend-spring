package br.ufpb.dcx.iago.lojadejogos.backend.exception;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
