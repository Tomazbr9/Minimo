package com.tomazbr9.minimo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tb_shortenedUrl")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShortenedUrl {

    private UUID id;
    private String shortenedUrl;
    private String originalUrl;

}
