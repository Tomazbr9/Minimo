package com.tomazbr9.minimo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_url")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Url {

    @Id
    @GeneratedValue
    private UUID id;

    private String shortenedUrl;
    private String originalUrl;

}
