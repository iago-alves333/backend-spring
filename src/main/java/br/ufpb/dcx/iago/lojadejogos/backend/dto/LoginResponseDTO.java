package br.ufpb.dcx.iago.lojadejogos.backend.dto;

/**
 * DTO para a resposta de login.
 * Retorna o token JWT que o cliente deve enviar no header de todas as próximas requisições.
 */
public class LoginResponseDTO {

    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
