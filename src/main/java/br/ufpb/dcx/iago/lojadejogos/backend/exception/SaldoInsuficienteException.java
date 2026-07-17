package br.ufpb.dcx.iago.lojadejogos.backend.exception;

/**
 * Lançada quando o usuário tenta comprar um jogo com saldo menor do que o preço.
 * Devolve status HTTP 400 (Bad Request).
 */
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
