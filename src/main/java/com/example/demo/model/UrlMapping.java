package com.example.demo.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_mapping_seq")
    @SequenceGenerator(
            name = "url_mapping_seq",
            sequenceName = "url_mapping_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(length = 16, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

