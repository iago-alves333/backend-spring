package br.ufpb.dcx.iago.lojadejogos.backend.dto;

/**
 * DTO (Data Transfer Object) utilizado internamente pelo backend para mapear a resposta
 * de autenticação da API da Twitch.
 *
 * <p>
 * O token recebido por esta classe é necessário para realizar requisições autenticadas
 * à API do IGDB e buscar os dados dos jogos.
 * </p>
 *
 * @param access_token O token de acesso gerado pela Twitch.
 * @param expires_in O tempo (em segundos) até a expiração do token.
 * @param token_type O tipo do token retornado (ex: "bearer").
 */
public record TwitchTokenResponse(String access_token, Integer expires_in, String token_type) {}
