package br.ufpb.dcx.iago.lojadejogos.backend.dto;

/**
 * DTO interno utilizado para mapear os dados do jogo recebidos diretamente da API do IGDB.
 *
 * @param id    O identificador único do jogo no IGDB.
 * @param name  O título original do jogo.
 * @param cover Objeto contendo os dados da capa do jogo ({@link IgdbCover}).
 */
public record IgdbGame(Long id, String name, IgdbCover cover) {}
