package com.tomazbr9.minimo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinimoApplication {

    private static final Logger logger = LoggerFactory.getLogger(MinimoApplication.class);


	public static void main(String[] args) {

        logger.info("Iniciando API de encurtar urls");
		SpringApplication.run(MinimoApplication.class, args);
        logger.info("API iniciada e pronta para receber requisições");
	}

}
