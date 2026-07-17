package br.ufpb.dcx.iago.lojadejogos.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO responsável por receber os dados do cliente para a criação ou atualização de um jogo.
 * Assegura a validação inicial (ex: campos obrigatórios) antes dos dados chegarem ao Service.
 */
public class JogoRequestDTO {
    // TODO: Adicionar atributos necessários para criar ou atualizar um Jogo a partir do JSON recebido
    // TODO: Adicionar getters e setters
    @NotBlank(message = "Nome é obrigatorio")
    @Size(min = 1 ,max = 100)
    private String nome;
    @NotBlank
    private String tipo;
    private String urlImagem;
    @DecimalMin(value = "0.0", message = "Não pode ser negativo")
    private BigDecimal preco;
    @NotBlank
    private String descricao;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
