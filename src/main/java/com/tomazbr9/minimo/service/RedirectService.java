package com.tomazbr9.minimo.service;

import com.tomazbr9.minimo.exception.UrlNotFoundException;
import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.repository.UrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {

    @Autowired
    private UrlRepository urlRepository;

    @Transactional
    public String getOriginalUrl(String shortUrl){

        System.out.println(shortUrl);

        Url url = urlRepository.findUrlByShortenedUrl(shortUrl).orElseThrow(() -> new UrlNotFoundException("Url não encontrada!"));

        url.incrementClicks();
        urlRepository.save(url);

        return url.getOriginalUrl();

    }
}
