package com.tomazbr9.minimo.service;

import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {

    @Autowired
    private UrlRepository urlRepository;


    public String getOriginalUrl(String shortUrl){
        Url url = urlRepository.findUrlByShortenedUrl(shortUrl);

        url.incrementClicks();
        urlRepository.save(url);

        return url.getOriginalUrl();

    }
}
