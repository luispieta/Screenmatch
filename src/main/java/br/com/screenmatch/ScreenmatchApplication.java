package br.com.screenmatch;

import br.com.screenmatch.Service.ConsumoApi;
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
//		var consumoApi = new ConsumoApi();
//		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=Rick+and+Morty&Season=1&apiKey=6585022c");
		System.out.println(json);

	}

}
