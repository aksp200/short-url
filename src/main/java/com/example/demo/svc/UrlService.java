package com.example.demo.svc;

import com.example.demo.model.RedirectCount;
import com.example.demo.model.UrlMapping;
import com.example.demo.repo.RedirectCountRepository;
import com.example.demo.repo.UrlMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UrlService {
    @Autowired
    private UrlMappingRepository repository;

    @Autowired
    private RedirectCountRepository redirectCountRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Value("${short.url.secret}")
    private String secretKey;

    public String shortenUrl(String originalUrl) {

        originalUrl = getUrlWithHttp(originalUrl);

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setCreatedAt(LocalDateTime.now());

        mapping = repository.save(mapping); // Save to get generated ID

        String shortCode = secureShortCode(mapping.getId());
        mapping.setShortCode(shortCode);
        repository.save(mapping);

        redirectCountRepository.save(new RedirectCount(mapping.getId(), 0));

        return shortCode;
    }

    private static String getUrlWithHttp(String originalUrl) {
        try {
            URI uri = new URI(originalUrl);
            if (uri.getScheme() == null) {
                originalUrl = "https://" + originalUrl;
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format: " + originalUrl, e);
        }
        return originalUrl;
    }

    @Transactional(readOnly = true)
    public Optional<String> resolveUrl(String code) {
        return repository.findByShortCode(code).map(mapping -> {
            // Delegate to async thread after transaction-safe values are extracted
            taskExecutor.execute(() -> redirectCountRepository.incrementCount(mapping.getId()));
            return mapping.getOriginalUrl();
        });
    }


    @Transactional
    public void updateRedirectCount(Long id) {
        redirectCountRepository.incrementCount(id);
    }

    private String secureShortCode(Long id) {
        try {
            // Introduce a salt for non-predictability
            String salt = UUID.randomUUID().toString();
            String input = id + ":" + salt;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hmac = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));

            byte[] shortBytes = Arrays.copyOf(hmac, 6);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(shortBytes);
        } catch (Exception e) {
            log.error("Error generating secure short code", e);
            throw new RuntimeException("Error generating secure short code", e);
        }
    }
}