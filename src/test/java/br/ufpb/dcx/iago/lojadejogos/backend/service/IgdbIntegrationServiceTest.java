package br.ufpb.dcx.iago.lojadejogos.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IgdbIntegrationServiceTest {

    // TODO: Criar @Mock para WebClient
    // TODO: Criar @InjectMocks para IgdbIntegrationService

    @Test
    void deveBuscarJogosExternosComSucesso() {
        // TODO: Simular as chamadas assíncronas/reativas do WebClient
        // TODO: Retornar lista de jogos simulada do IGDB
        // TODO: Verificar se a requisição tratou os headers da Twitch corretamente
    }

    @Test
    void deveTratarErroDaApiExterna() {
        // TODO: Simular um retorno 4xx ou 5xx da API do IGDB
        // TODO: Verificar qual exceção customizada ou erro é devolvido pelo serviço
    }
}
