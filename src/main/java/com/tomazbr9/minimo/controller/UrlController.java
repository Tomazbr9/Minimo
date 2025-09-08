package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/url")
public class UrlController {

    @Autowired
    private UrlService service;

    @PostMapping
    public ResponseEntity<Url> createShortUrl(@RequestBody Url request){
        Url url = service.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(url);

    }

}
