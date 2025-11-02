package com.tomazbr9.linkshort.controller;

import com.tomazbr9.linkshort.service.RedirectService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class RedirectController {

    private static final Logger logger = LoggerFactory.getLogger(RedirectController.class);

    @Autowired
    private RedirectService service;

    @GetMapping("/{shortUrl}")
    public void redirectOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = service.getOriginalUrl(shortUrl);

        if(originalUrl != null){
            response.sendRedirect(originalUrl);
            logger.info("Usuário sendo redirecionado para pagina da url original");
        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Url não encontrada!");
            logger.warn("Url não encontrada.");
        }
    }
}
