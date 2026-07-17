package br.ufpb.dcx.iago.lojadejogos.backend.dto;

import java.math.BigDecimal;

/**
 * DTO utilizado para enviar os detalhes de um jogo para o cliente.
 * Expõe as informações formatadas adequadamente para as interfaces de vitrine e catálogo.
 */
public class JogoResponseDTO {
    // TODO: Adicionar atributos que serão enviados de volta para o cliente (ex: JavaFX)
    // TODO: Adicionar getters e setters
    private String nome;
    private String tipo;
    private BigDecimal preco;
    private String urlImagem;
    private Long id;
    private String descricao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
