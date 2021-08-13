package com.shorturl.svc;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ShortenerSvcImpl implements ShortenerSvc {
    @Override
    public String getShortUrlKey(String longUrl) {
        return Base64.getEncoder().encodeToString((longUrl.hashCode()+"").getBytes());
    }
}
