package com.tomazbr9.minimo.repository;

import com.tomazbr9.minimo.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

    boolean existsByShortenedUrl(String url);
}
