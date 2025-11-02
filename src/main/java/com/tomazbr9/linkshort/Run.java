package com.tomazbr9.linkshort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Run {

    private static final Logger logger = LoggerFactory.getLogger(Run.class);


	public static void main(String[] args) {

        logger.info("Iniciando API de encurtar urls");
		SpringApplication.run(Run.class, args);
        logger.info("API iniciada e pronta para receber requisições");
	}

}
