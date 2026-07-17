package br.ufpb.dcx.iago.lojadejogos.backend.exception;

/**
 * Exceção lançada quando se tenta salvar um jogo com preço inválido (negativo).
 * Essa é uma regra de NEGÓCIO, diferente da validação de formato no DTO (@DecimalMin).
 * O DTO valida o formato, o Service valida a regra de negócio.
 */
public class PrecoInvalidoException extends RuntimeException {
    public PrecoInvalidoException(String message) {
        super(message);
    }
}
