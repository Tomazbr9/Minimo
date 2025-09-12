package com.tomazbr9.minimo.controller;

import com.tomazbr9.minimo.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/url")
public class UrlController {

    @Autowired
    private UrlService service;

    @PostMapping
    public ResponseEntity<UrlResponseDTO> createShortUrl(@RequestBody UrlRequestDTO request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        UrlResponseDTO response = service.createShortUrl(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
