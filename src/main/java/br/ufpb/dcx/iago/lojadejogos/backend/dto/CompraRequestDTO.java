package br.ufpb.dcx.iago.lojadejogos.backend.dto;


public class CompraRequestDTO {

    private Long userId;
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

