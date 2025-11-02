package com.tomazbr9.linkshort.service;

import com.tomazbr9.linkshort.exception.UrlNotFoundException;
import com.tomazbr9.linkshort.model.Url;
import com.tomazbr9.linkshort.repository.UrlRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {

    private static final Logger logger = LoggerFactory.getLogger(RedirectService.class);

    @Autowired
    private UrlRepository urlRepository;

    @Transactional
    public String getOriginalUrl(String shortUrl){

        Url url = urlRepository.findUrlByShortenedUrl(shortUrl).orElseThrow(() -> new UrlNotFoundException("Url não encontrada!"));

        url.incrementClicks();

        logger.info("Atulizado o numero de acessos que url tem.");

        urlRepository.save(url);

        return url.getOriginalUrl();

    }
}
