package com.tomazbr9.linkshort.util;

import java.security.SecureRandom;

public class ShortUrlGenerator {

    // Conjunto de caracteres que podem aparecer na URL curta
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";

    // Tamanho padrão da URL curta
    private static final int DEFAULT_LENGTH = 8;

    // Gerador de números aleatórios seguro e menos previsível que Random
    private static final SecureRandom random = new SecureRandom();

    // Método responsável por gerar a URL curta
    public static String generateShortUrl() {

        // StringBuilder é eficiente para construir strings em loop
        StringBuilder sb = new StringBuilder(DEFAULT_LENGTH);

        // Loop que escolhe caracteres aleatórios até formar a string de tamanho DEFAULT_LENGTH
        for (int i = 0; i < DEFAULT_LENGTH; i++) {

            // Gera um índice aleatório dentro do conjunto ALPHANUMERIC
            int index = random.nextInt(ALPHANUMERIC.length());

            // Pega o caractere correspondente e adiciona ao StringBuilder
            sb.append(ALPHANUMERIC.charAt(index));
        }

        // Retorna a string final gerada (URL curta)
        return sb.toString();
    }
}

