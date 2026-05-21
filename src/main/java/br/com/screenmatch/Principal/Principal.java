package br.com.screenmatch.Principal;

import br.com.screenmatch.Service.ConsumoApi;
import br.com.screenmatch.Service.ConverteDados;
import br.com.screenmatch.domain.Episodio.Episodio;
import br.com.screenmatch.domain.Series.DadosEpisodio;
import br.com.screenmatch.domain.Series.DadosSerie;
import br.com.screenmatch.domain.Series.DadosTemporada;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apiKey=6585022c";
    private final String SEASON = "&Season=";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public void exibeMenu() throws JsonProcessingException {

        System.out.println("Digite o nome da série para a busca: ");
        var nomeSerie = leitura.nextLine();

        // ----- TEMPORADA -----
        System.out.println("----- DADOS DA TEMPORADA -----");
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        String dadosSerieFormatado = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dadosSerie);
        System.out.println(dadosSerieFormatado);

        // ----- TEMPORADAS COM EPISÓDIOS -----
        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + SEASON + i + API_KEY);

            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			String dadosTemporadaFormatado = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dadosTemporada);
            temporadas.add(dadosTemporada);

			System.out.println("\n--- TEMPORADA " + i + " ---");
			System.out.println(dadosTemporadaFormatado);

		}
        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        // ----- TOP 5 EPISÓDIOS -----
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .map(e -> e.titulo().toUpperCase())
                .forEach(System.out::println);

        // ----- EPISÓDIOS QUE TEM NAS TEMPORADAS -----
        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.temporada(), d)))
                .collect(Collectors.toList());

        episodio.forEach(System.out::println);

        // ----- FILTRO PELO TRECHO DO TÍTULO DO EPISÓDIO -----
        System.out.println("Digite o nome do episódio: ");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioBuscado = episodio.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if(episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado.");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
            System.out.println("Nome do episódio: " + episodioBuscado.get().getTitulo());

        } else {
            System.out.println("Episódio não encontrado.");
        }

        // ----- FILTRO POR ANO -----
        System.out.println("A partir que ano você deseja ver os episodios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodio.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                        " Episodio: " + e.getTitulo() +
                        " Avaliação: " + e.getAvaliacao() +
                        " Data lançamento: " + e.getDataLancamento().format(formatador)
                ));

    }

}
