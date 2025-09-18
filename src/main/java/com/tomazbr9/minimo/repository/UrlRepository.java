package com.tomazbr9.minimo.repository;

import com.tomazbr9.minimo.model.Url;
import com.tomazbr9.minimo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

    boolean existsByShortenedUrl(String url);

    List<Url> findUrlByUser(User user);

    Url findUrlByShortenedUrl(String shortUrl);
}
