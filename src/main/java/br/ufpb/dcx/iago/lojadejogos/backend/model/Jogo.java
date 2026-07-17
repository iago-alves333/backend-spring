package br.ufpb.dcx.iago.lojadejogos.backend.model;
import jakarta.persistence.*;

import java.math.BigDecimal;

// TODO: Adicionar anotação @Entity para mapear a classe como tabela no banco de dados
@Entity
public class Jogo {
    // TODO: Adicionar @Id e @GeneratedValue para o identificador (chave primária)
    // TODO: Adicionar demais atributos do jogo (nome, preco, categoria, etc.)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal preco;
    private String tipo;
    private String urlImagem;

    @Column(columnDefinition = "TEXT")
    private String descricao;


    public Jogo() {
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
