package br.ufpb.dcx.iago.lojadejogos.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

// TODO: Adicionar anotação @Entity
@Entity
@Table(name = "usuarios")
public class User {
    // TODO: Adicionar @Id e @GeneratedValue para o identificador
    // TODO: Adicionar demais atributos do usuário (nome, email, senha, etc.)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal saldo;
    private String senha;
    private boolean isAdmin;
    @ManyToMany
    @JoinTable(
            name = "usuario_jogos",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "jogo_id")
    )
    private List<Jogo> jogos;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }
}
