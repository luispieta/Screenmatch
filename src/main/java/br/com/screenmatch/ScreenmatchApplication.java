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
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=Rick+and+Morty&apiKey=6585022c");
		ConverteDados conversor = new ConverteDados();
		DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);

		json = consumoApi.obterDados("https://www.omdbapi.com/?t=Rick+and+Morty&Season=1&apiKey=6585022c");
		DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
		System.out.println(dadosTemporada);

		json = consumoApi.obterDados("https://www.omdbapi.com/?t=Rick+and+Morty&Season=1&Episode=2&apiKey=6585022c");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

	}

}
