package br.com.screenmatch;

import br.com.screenmatch.Service.ConsumoApi;
import br.com.screenmatch.Service.ConverteDados;
import br.com.screenmatch.domain.Series.DadosEpisodio;
import br.com.screenmatch.domain.Series.DadosSerie;
import br.com.screenmatch.domain.Series.DadosTemporada;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var conversor = new ConverteDados();

		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=Rick+and+Morty&apiKey=6585022c");
		DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);

		System.out.println("--- DADOS DA SÉRIE ---");
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dadosSerie));

		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			json = consumoApi.obterDados("https://www.omdbapi.com/?t=Rick+and+Morty&Season=" + i + "&apiKey=6585022c");

			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			String jsonFormatado = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dadosTemporada);

			System.out.println("\n--- TEMPORADA " + i + " ---");
			System.out.println(jsonFormatado);
		}
	}

}
