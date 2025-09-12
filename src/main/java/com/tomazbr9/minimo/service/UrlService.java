package com.tomazbr9.minimo.service;


import com.tomazbr9.minimo.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.minimo.exception.UrlAlreadyExistsException;
import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.repository.UrlRepository;
import com.tomazbr9.minimo.repository.UserRepository;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.util.ShortUrlGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UrlResponseDTO createShortUrl(UrlRequestDTO request, UserDetailsImpl userDetails){

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if(urlRepository.existsByShortenedUrl(request.shortenedUrl())){
            throw new UrlAlreadyExistsException("Url já existe!");
        }

        String newUrl = request.shortenedUrl() != null && !request.shortenedUrl().isBlank() ? request.shortenedUrl() : ShortUrlGenerator.generateShortUrl();

        // Constroi a url e gera uma url encurtada
        Url url = Url.builder()
                .originalUrl(request.originalUrl())
                .shortenedUrl(newUrl)
                .user(user)
                .build();

        urlRepository.save(url);

        return new UrlResponseDTO(url.getId(), url.getShortenedUrl(), url.getOriginalUrl());
    }

}
