package br.ufpb.dcx.iago.lojadejogos.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO padronizado para representar mensagens de erro da API.
 * Contém o status HTTP, mensagem, data e hora, e opcionalmente uma lista de erros de validação.
 */
public class ErrorResponseDTO {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;
    private List<String> erros;

    public ErrorResponseDTO() {
    }
    public ErrorResponseDTO(int status, String mensagem, LocalDateTime timestamp, List<String> erros) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
        this.erros = erros;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }
}
