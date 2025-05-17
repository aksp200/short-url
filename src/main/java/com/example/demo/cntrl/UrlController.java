package com.example.demo.cntrl;

import com.example.demo.dto.Url;
import com.example.demo.svc.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class UrlController {
    @Autowired
    UrlService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody Url url) {
        return ResponseEntity.ok(service.shortenUrl(url.getUrl()));
    }

    @GetMapping("/s/{code}")
    public ResponseEntity<Object> redirect(@PathVariable String code) {
        //log.info("service.resolveUrl(code).get() : {}",service.resolveUrl(code).get());
        return service.resolveUrl(code)
                .map(url -> ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build())
                .orElse(ResponseEntity.notFound().build());
    }
}