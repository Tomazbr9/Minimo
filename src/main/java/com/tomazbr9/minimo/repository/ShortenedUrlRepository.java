package com.tomazbr9.minimo.repository;

import com.tomazbr9.minimo.model.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, UUID> {
}
