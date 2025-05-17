package com.example.demo.model;

import com.example.demo.model.UrlMapping;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RedirectCount {

    @Id
    @Column(name = "url_mapping_id")
    @NonNull
    private Long urlMappingId;

    @NonNull
    private int count;
}
