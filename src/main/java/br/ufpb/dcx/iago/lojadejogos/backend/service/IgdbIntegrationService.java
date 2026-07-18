package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.IgdbGame;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoBuscaExternaDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.TwitchTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IgdbIntegrationService {

    private final WebClient webClient;

    @Value("${twitch.client.id}")
    private String clientId;

    @Value("${twitch.client.secret}")
    private String clientSecret;

    public IgdbIntegrationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    private String getTwitchAccessToken() {
        // CORREÇÃO: Passando a URL e parâmetros em um único método .uri()
        TwitchTokenResponse response = webClient.post()
                .uri("https://id.twitch.tv/oauth2/token?client_id={id}&client_secret={secret}&grant_type=client_credentials",
                        clientId, clientSecret)
                .retrieve()
                .bodyToMono(TwitchTokenResponse.class)
                .block();

        if (response != null) {
            return response.access_token();
        }
        throw new RuntimeException("Falha ao obter token da Twitch");
    }

    public List<JogoBuscaExternaDTO> buscarJogosPorNome(String nomeDoJogo) {
        String accessToken = getTwitchAccessToken();

        String query = String.format("search \"%s\"; fields name, cover.url; limit 5;", nomeDoJogo);

        IgdbGame[] response = webClient.post()
                .uri("https://api.igdb.com/v4/games")
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(query)
                .retrieve()
                .bodyToMono(IgdbGame[].class)
                .block();

        if (response == null || response.length == 0) {
            return new ArrayList<>();
        }

        return List.of(response).stream().map(game -> {
            String imageUrl = "";

            // CORREÇÃO: Se você fez IgdbGame como "class", troque os métodos abaixo para .getCover() e .getUrl()
            if (game.cover() != null && game.cover().url() != null) {
                imageUrl = "https:" + game.cover().url();
                imageUrl = imageUrl.replace("t_thumb", "t_cover_big");
            }

            // CORREÇÃO: Se você fez IgdbGame como "class", troque .name() para .getName()
            return new JogoBuscaExternaDTO(game.name(), imageUrl);
        }).collect(Collectors.toList());
    }
}