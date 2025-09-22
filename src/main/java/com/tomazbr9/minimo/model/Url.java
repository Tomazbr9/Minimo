package com.tomazbr9.minimo.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_url")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class Url implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    private String shortenedUrl;

    @Lob
    @Column(nullable = false)
    private String originalUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
