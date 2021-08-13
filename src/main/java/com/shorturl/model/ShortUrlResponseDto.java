package com.shorturl.model;

import org.hibernate.validator.constraints.URL;

import java.util.Objects;

public class ShortUrlResponseDto extends ShortUrlRequestDto{

    @URL
    String shortUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public ShortUrlResponseDto setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShortUrlResponseDto that = (ShortUrlResponseDto) o;
        return Objects.equals(shortUrl, that.shortUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shortUrl);
    }

    @Override
    public String toString() {
        return "ShortUrlResponseDto{" +
                "shortUrl='" + shortUrl + '\'' +
                ", longUrl='" + longUrl + '\'' +
                '}';
    }
}
