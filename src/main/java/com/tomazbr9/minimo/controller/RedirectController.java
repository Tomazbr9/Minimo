package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.service.RedirectService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class RedirectController {

    @Autowired
    private RedirectService service;

    @GetMapping("/{shortUrl}")
    public void redirectOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = service.getOriginalUrl(shortUrl);

        if(originalUrl != null){
            response.sendRedirect(originalUrl);
        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Url não encontrada!");
        }
    }
}
