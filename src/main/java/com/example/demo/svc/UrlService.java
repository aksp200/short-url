package com.example.demo.svc;

import com.example.demo.model.UrlMapping;
import com.example.demo.repo.UrlMappingRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class UrlService {
    @Autowired UrlMappingRepository repository;

    @Value("${short.url.secret}")
    private String secretKey;

    public String shortenUrl(String originalUrl) {
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setRedirectCount(0);

        mapping = repository.save(mapping); // ensure ID is generated
        String shortCode = secureShortCode(mapping.getId());
        mapping.setShortCode(shortCode);
        repository.save(mapping);

        return shortCode;
    }

    public Optional<String> resolveUrl(String code) {
        return repository.findByShortCode(code).map(mapping -> {
            mapping.setRedirectCount(mapping.getRedirectCount() + 1);
            repository.save(mapping);
            return mapping.getOriginalUrl();
        });
    }

    private String secureShortCode(Long id) {
        try {
            byte[] raw = ByteBuffer.allocate(Long.BYTES).putLong(id).array();
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hmac = mac.doFinal(raw);

            // Use first 5 bytes only for shorter output
            byte[] shortBytes = Arrays.copyOf(hmac, 5);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(shortBytes);
        } catch (Exception e) {
            log.error("Error generating secure short code", e);
            throw new RuntimeException("Error generating secure short code", e);
        }
    }
}