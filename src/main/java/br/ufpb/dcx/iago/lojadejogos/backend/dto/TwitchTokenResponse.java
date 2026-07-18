package br.ufpb.dcx.iago.lojadejogos.backend.dto;

public record TwitchTokenResponse(String access_token, Integer expires_in, String token_type) {}
