package com.shorturl.ctrl;

import com.shorturl.model.ShortUrlRequestDto;
import com.shorturl.model.ShortUrlResponseDto;
import com.shorturl.svc.ShortenerAccessSvc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController("v1")
public class Shortener {

    private ShortenerAccessSvc shortenerAccessSvc;

    public Shortener(ShortenerAccessSvc shortenerAccessSvc) {
        this.shortenerAccessSvc = shortenerAccessSvc;
    }

    @PostMapping("/short")
    public ResponseEntity<ShortUrlResponseDto> createShort(@RequestBody @Valid ShortUrlRequestDto shortUrlRequestDto)
            throws IOException, URISyntaxException {
        return new ResponseEntity<>(shortenerAccessSvc.createShortUrl(shortUrlRequestDto), CREATED);
    }
}
