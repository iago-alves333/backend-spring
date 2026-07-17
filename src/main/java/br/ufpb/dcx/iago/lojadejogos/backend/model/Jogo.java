package br.ufpb.dcx.iago.lojadejogos.backend.model;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidade que representa um jogo disponível no catálogo da loja.
 * Contém informações essenciais para a vitrine, como preço, tipo e url da imagem,
 * e é referenciado em compras e na biblioteca dos usuários.
 */
@Entity
public class Jogo {
    
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
