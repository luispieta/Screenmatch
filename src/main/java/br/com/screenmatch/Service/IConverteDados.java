package br.com.screenmatch.Service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
