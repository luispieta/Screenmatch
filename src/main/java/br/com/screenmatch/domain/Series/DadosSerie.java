package br.com.screenmatch.domain.Series;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosSerie(
                        @JsonAlias("Title") String titulo,
                        @JsonAlias("totalSeasons") Integer totalTemporadas,
                        @JsonAlias("imdbRating") String avaliacao,
                        @JsonAlias("Genre") String genero,
                        @JsonAlias("Poster") String poster
) {

}
