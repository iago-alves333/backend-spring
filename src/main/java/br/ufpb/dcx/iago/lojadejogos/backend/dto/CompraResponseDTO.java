package br.ufpb.dcx.iago.lojadejogos.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de saída que representa o recibo de uma compra efetuada com sucesso.
 * Transmite as informações consolidadas da transação (nome do usuário, jogo, valor pago, data) para o cliente.
 */
public class CompraResponseDTO {

    private Long idCompra;

    private String nomeUsuario;
    private String nomeJogo;

    private LocalDateTime dataCompra;
    private BigDecimal preco;

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeJogo() {
        return nomeJogo;
    }

    public void setNomeJogo(String nomeJogo) {
        this.nomeJogo = nomeJogo;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}