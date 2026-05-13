package br.com.screenmatch.domain.Series;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(
                            @JsonAlias("Title")
                            String titulo,
                            @JsonAlias("Episode")
                            Integer episodio,
                            @JsonAlias("imdbRating")
                            String avaliação,
                            @JsonAlias("Released")
                            String dataLancamento
) { }
