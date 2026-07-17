package br.ufpb.dcx.iago.lojadejogos.backend.dto;

import java.math.BigDecimal;

/**
 * DTO utilizado para devolver os dados do usuário ao cliente de forma segura.
 * Oculta informações sensíveis como senhas, garantindo a segurança na exposição da API.
 */
public class UserResponseDTO {
    private Long idUser;
    private String nome;
    private BigDecimal saldo;
    private boolean isAdmin;

    public Long getIdUser() {
        return idUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;

    }
}
