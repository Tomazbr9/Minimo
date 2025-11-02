package com.tomazbr9.minimo.service;


import com.tomazbr9.minimo.dto.urlDTO.TotalClicksAndMostClickedDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlPutDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlRequestDTO;
import com.tomazbr9.minimo.dto.urlDTO.UrlResponseDTO;
import com.tomazbr9.minimo.exception.PermissionDeniedToAccessResourceException;
import com.tomazbr9.minimo.exception.UrlAlreadyExistsException;
import com.tomazbr9.minimo.exception.UrlNotFoundException;
import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.model.User;
import com.tomazbr9.minimo.repository.UrlRepository;
import com.tomazbr9.minimo.repository.UserRepository;
import com.tomazbr9.minimo.security.model.UserDetailsImpl;
import com.tomazbr9.minimo.util.ShortUrlGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UrlService {

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<UrlResponseDTO> findUrls(UserDetailsImpl userDetails){

        User user = checkIfUserExists(userDetails);

        List<Url> urlsList = urlRepository.findUrlByUser(user);

        return urlsList
                .stream()
                .map(
                        url -> new UrlResponseDTO(
                                url.getId(),
                                url.getUrlName(),
                                url.getTotalClicks(),
                                url.getShortenedUrl(),
                                url.getOriginalUrl(),
                                url.getCreatedIn()
                        )

                )
                .toList();
    }

    @Transactional
    public UrlResponseDTO findUrlById(UUID id, UserDetailsImpl userDetails){

        Url url = checkIfUrlExists(id);
        checkIfResourceBelongsToUser(userDetails, url);
        return new UrlResponseDTO(url.getId(), url.getUrlName(), url.getTotalClicks(), url.getShortenedUrl(), url.getOriginalUrl(), url.getCreatedIn());

    }

    @Transactional
    public UrlResponseDTO createShortUrl(UrlRequestDTO request, UserDetailsImpl userDetails){

        User user = checkIfUserExists(userDetails);

        if(urlRepository.existsByShortenedUrl(request.shortenedUrl())){
            throw new UrlAlreadyExistsException("Url já existe!");
        }

        String newUrl = request.shortenedUrl() != null && !request.shortenedUrl().isBlank() ? request.shortenedUrl() : ShortUrlGenerator.generateShortUrl();

        // Constroi a url e gera uma url encurtada
        Url url = Url.builder()
                .urlName(request.urlName())
                .originalUrl(request.originalUrl())
                .shortenedUrl(newUrl)
                .createdIn(LocalDate.now())
                .user(user)
                .build();

        urlRepository.save(url);

        logger.info("Url salva no banco de dados");

        return new UrlResponseDTO(url.getId(), url.getUrlName(), url.getTotalClicks(), url.getShortenedUrl(), url.getOriginalUrl(), url.getCreatedIn());
    }

    @Transactional
    public UrlPutDTO putUrl(UUID id, UrlPutDTO request, UserDetailsImpl userDetails){
        Url url = checkIfUrlExists(id);

        url.setUrlName(request.urlName());
        url.setOriginalUrl(request.originalUrl());

        urlRepository.save(url);

        logger.info("Url atualizada e salva no banco  de dados");

        return new UrlPutDTO(url.getUrlName(), url.getOriginalUrl());

    }

    @Transactional
    public void deleteUrl(UUID id, UserDetailsImpl userDetails){
        Url url = checkIfUrlExists(id);
        checkIfResourceBelongsToUser(userDetails, url);

        urlRepository.delete(url);

        logger.info("Url deletada do banco de dados");
    }

    @Transactional
    public TotalClicksAndMostClickedDTO totalClicksOfAllUrls(UserDetailsImpl userDetails){

        User user = checkIfUserExists(userDetails);

        List<Url> urlsList = urlRepository.findUrlByUser(user);

        Integer totalClicks = 0;
        Integer mostClicked = 0;

        for(Url url : urlsList){
            totalClicks += url.getTotalClicks();

            if (mostClicked < url.getTotalClicks()){
                mostClicked = url.getTotalClicks();
            }
        }

        return new TotalClicksAndMostClickedDTO(totalClicks, mostClicked);

    }

    private User checkIfUserExists(UserDetailsImpl userDetails){
        return userRepository.findByUsername(
                userDetails.getUsername()
        ).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }

    private void checkIfResourceBelongsToUser(UserDetailsImpl userDetails, Url url){
        if (!url.getUser().getUsername().equals(userDetails.getUsername())){
            throw new PermissionDeniedToAccessResourceException("Sem permissão ao recurso");
        }
    }

    private Url checkIfUrlExists(UUID id) {
        return urlRepository.findById(id).orElseThrow(() -> new UrlNotFoundException("Url não encontrada!"));
    }

}
