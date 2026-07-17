package br.ufpb.dcx.iago.lojadejogos.backend.dto;


import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada que recebe o identificador do usuário e do jogo para processar uma nova transação de compra.
 * Age como um envelope de transporte dos dados mínimos necessários para a transação.
 */
public class CompraRequestDTO {
    @NotNull(message = "Id do usuário é obrigatorio")
    private Long userId;
    @NotNull(message = "ID do jogo é obrigatório")
    private Long jogoId;

    // Getters e Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJogoId() {
        return jogoId;
    }

    public void setJogoId(Long jogoId) {
        this.jogoId = jogoId;
    }
}

