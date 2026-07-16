package br.ufpb.dcx.iago.lojadejogos.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO de criação/registro de usuário.
 * NOTA DE SEGURANÇA: O campo isAdmin foi REMOVIDO propositalmente deste DTO.
 * Se ele estivesse aqui, qualquer pessoa poderia se registrar como Admin
 * enviando {"isAdmin": true} no JSON. O valor padrão de isAdmin é false.
 * Apenas um Admin autenticado pode promover outro usuário via endpoint separado.
 */
public class UserRequestDTO {
    @NotBlank
    @Size(min = 2, max = 50)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal saldo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
