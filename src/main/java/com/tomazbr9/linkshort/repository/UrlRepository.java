package com.tomazbr9.linkshort.repository;

import com.tomazbr9.linkshort.model.Url;
import com.tomazbr9.linkshort.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

    boolean existsByShortenedUrl(String url);

    List<Url> findUrlByUser(User user);

    Optional<Url> findUrlByShortenedUrl(String shortUrl);
}
