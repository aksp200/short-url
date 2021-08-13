package com.shorturl.svc;

import com.shorturl.model.ShortUrlRequestDto;
import com.shorturl.model.ShortUrlResponseDto;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ShortenerAccessSvc {
    ShortUrlResponseDto createShortUrl(ShortUrlRequestDto shortUrlRequestDto) throws IOException, URISyntaxException;
    String getLongUrl(String shortUrlKey);
    String getShortUrl(String longUrl);
}
