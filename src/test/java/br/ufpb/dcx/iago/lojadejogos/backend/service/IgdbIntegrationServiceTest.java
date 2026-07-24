package br.ufpb.dcx.iago.lojadejogos.backend.service;

import br.ufpb.dcx.iago.lojadejogos.backend.dto.IgdbCover;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.IgdbGame;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.JogoBuscaExternaDTO;
import br.ufpb.dcx.iago.lojadejogos.backend.dto.TwitchTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para IgdbIntegrationService.
 * Simula as chamadas reativas do WebClient para a API do IGDB e Twitch.
 */
@ExtendWith(MockitoExtension.class)
public class IgdbIntegrationServiceTest {

    // Mocks das partes fluentes do WebClient
    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    // Instância real do serviço com injeção manual do WebClient mockado
    private IgdbIntegrationService igdbIntegrationService;

    @BeforeEach
    void setUp() {
        // Configura o builder para retornar o WebClient mockado
        when(webClientBuilder.build()).thenReturn(webClient);

        igdbIntegrationService = new IgdbIntegrationService(webClientBuilder);

        // Injeta as propriedades que viriam do application.properties
        ReflectionTestUtils.setField(igdbIntegrationService, "clientId", "fake-client-id");
        ReflectionTestUtils.setField(igdbIntegrationService, "clientSecret", "fake-client-secret");
    }

    @Test
    @SuppressWarnings("unchecked")
    void deveBuscarJogosExternosComSucesso() {
        // --- Passo 1: Simula a chamada ao Twitch para obter o token ---
        TwitchTokenResponse twitchToken = new TwitchTokenResponse("fake-access-token", 3600, "bearer");

        // Mock para a cadeia fluente do WebClient.post() — chamada do Twitch
        WebClient.RequestBodySpec twitchBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec twitchResponseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        // Primeira chamada: Twitch token (URI com variáveis)
        when(requestBodyUriSpec.uri(anyString(), any(Object[].class))).thenReturn(twitchBodySpec);
        when(twitchBodySpec.retrieve()).thenReturn(twitchResponseSpec);
        when(twitchResponseSpec.bodyToMono(TwitchTokenResponse.class)).thenReturn(Mono.just(twitchToken));

        // --- Passo 2: Simula a chamada ao IGDB com o token obtido ---
        // IgdbGame tem 3 campos: id, name, cover
        IgdbCover cover = new IgdbCover(1L, "//images.igdb.com/igdb/image/upload/t_thumb/abc.jpg");
        IgdbGame[] jogosRetornados = {
                new IgdbGame(1L, "The Witcher 3", cover),
                new IgdbGame(2L, "The Witcher 2", null)
        };

        WebClient.RequestBodySpec igdbBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec igdbResponseSpec = mock(WebClient.ResponseSpec.class);

        // Segunda chamada: IGDB (URI fixa)
        when(requestBodyUriSpec.uri("https://api.igdb.com/v4/games")).thenReturn(igdbBodySpec);
        when(igdbBodySpec.header(eq("Client-ID"), anyString())).thenReturn(igdbBodySpec);
        when(igdbBodySpec.header(eq("Authorization"), anyString())).thenReturn(igdbBodySpec);
        when(igdbBodySpec.contentType(any(MediaType.class))).thenReturn(igdbBodySpec);
        when(igdbBodySpec.bodyValue(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(igdbResponseSpec);
        when(igdbResponseSpec.bodyToMono(IgdbGame[].class)).thenReturn(Mono.just(jogosRetornados));

        // Executa o método
        List<JogoBuscaExternaDTO> resultado = igdbIntegrationService.buscarJogosPorNome("witcher");

        // Verifica se a requisição retornou a lista correta e tratou os headers da Twitch
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nome()).isEqualTo("The Witcher 3");
        // URL da imagem deve ter sido transformada de t_thumb para t_cover_big
        assertThat(resultado.get(0).urlImagem()).contains("t_cover_big");
        assertThat(resultado.get(0).urlImagem()).startsWith("https:");
        // Jogo sem imagem deve retornar URL vazia
        assertThat(resultado.get(1).urlImagem()).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void deveTratarErroDaApiExterna() {
        // Simula falha ao obter token da Twitch (Mono.empty() → block() retorna null)
        WebClient.RequestBodySpec twitchBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec twitchResponseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(), any(Object[].class))).thenReturn(twitchBodySpec);
        when(twitchBodySpec.retrieve()).thenReturn(twitchResponseSpec);
        // Simula retorno nulo (falha na resposta da Twitch)
        when(twitchResponseSpec.bodyToMono(TwitchTokenResponse.class)).thenReturn(Mono.empty());

        // Verifica qual exceção é devolvida pelo serviço — RuntimeException com mensagem descritiva
        assertThatThrownBy(() -> igdbIntegrationService.buscarJogosPorNome("qualquer"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Falha ao obter token da Twitch");
    }
}
