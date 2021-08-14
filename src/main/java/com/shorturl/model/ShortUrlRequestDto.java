package com.shorturl.model;

import org.hibernate.validator.constraints.URL;

import java.util.Objects;

public class ShortUrlRequestDto {

    @URL
    String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public ShortUrlRequestDto setLongUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortUrlRequestDto that = (ShortUrlRequestDto) o;
        return Objects.equals(longUrl, that.longUrl) &&
                Objects.equals(longUrl, that.longUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longUrl, longUrl);
    }

    @Override
    public String toString() {
        return "ShortUrlRequestDto{" +
                "longUrl='" + longUrl + '\'' +
                '}';
    }
}
