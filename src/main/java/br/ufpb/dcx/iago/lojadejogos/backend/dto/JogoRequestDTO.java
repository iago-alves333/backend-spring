package br.ufpb.dcx.iago.lojadejogos.backend.dto;

import java.math.BigDecimal;

public class JogoRequestDTO {
    // TODO: Adicionar atributos necessários para criar ou atualizar um Jogo a partir do JSON recebido
    // TODO: Adicionar getters e setters
    private String nome;
    private String tipo;
    private String urlImagem;
    private BigDecimal preco;

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
    public String getUrlImagem() {
        return urlImagem;
    }
    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
    public BigDecimal getPreco() {
        return preco;
    }
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
