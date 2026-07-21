package br.ufpb.dcx.iago.lojadejogos.backend.dto;

/**
 * DTO de saída utilizado para enviar os resultados simplificados das buscas
 * de jogos ao cliente (Frontend).
 *
 * <h2>Uso no Frontend</h2>
 * <p>
 * Quando o Frontend faz uma requisição buscando um jogo, ele receberá um Array
 * contendo objetos JSON neste formato:
 * </p>
 * <pre>
 * [
 *   {
 *     "nome": "Super Mario 64",
 *     "urlImagem": "https://images.igdb.com/igdb/image/upload/t_cover_big/co1wca.jpg"
 *   }
 * ]
 * </pre>
 * <p>
 * O desenvolvedor Frontend pode utilizar esses dados diretamente para exibir
 * cartões de jogos. Por exemplo, em React:
 * </p>
 * <pre>
 * {@code
 * return (
 *   <div className="game-card">
 *     <img src={jogo.urlImagem} alt={`Capa do jogo ${jogo.nome}`} />
 *     <h3>{jogo.nome}</h3>
 *   </div>
 * );
 * }
 * </pre>
 *
 * @param nome      O título do jogo encontrado na API.
 * @param urlImagem A URL completa e pronta para uso da capa do jogo em alta resolução.
 */
public record JogoBuscaExternaDTO(String nome, String urlImagem) {}
