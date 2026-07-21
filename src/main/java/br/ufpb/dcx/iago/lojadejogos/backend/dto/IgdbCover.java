package br.ufpb.dcx.iago.lojadejogos.backend.dto;

/**
 * DTO interno utilizado para mapear o objeto de capa ("cover") retornado pela API do IGDB.
 *
 * @param id  O identificador único da capa no banco de dados do IGDB.
 * @param url A URL parcial da imagem da capa fornecida pelo IGDB.
 */
public record IgdbCover(Long id, String url) {}

