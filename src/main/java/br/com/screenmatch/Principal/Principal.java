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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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

        System.out.println("----- DADOS DA TEMPORADA -----");
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        String dadosSerieFormatado = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dadosSerie);
        System.out.println(dadosSerieFormatado);

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

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao)
                .reversed()).limit(5)
                .forEach(System.out::println);

        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.temporada(), d)))
                .collect(Collectors.toList());

        episodio.forEach(System.out::println);

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
                        " Data lançamento: " + e.getDataLancamento().format(formatador)
                ));

    }

}
