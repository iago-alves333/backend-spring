package br.ufpb.dcx.iago.lojadejogos.backend.dto;


import jakarta.validation.constraints.NotNull;

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

