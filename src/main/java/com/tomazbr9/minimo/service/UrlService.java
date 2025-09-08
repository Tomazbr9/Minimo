package com.tomazbr9.minimo.service;


import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public Url createShortUrl(Url url){

        return urlRepository.save(url);
    }

}
