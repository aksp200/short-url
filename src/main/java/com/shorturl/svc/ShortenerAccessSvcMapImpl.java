package com.shorturl.svc;

import com.shorturl.model.ShortUrlRequestDto;
import com.shorturl.model.ShortUrlResponseDto;
import com.shorturl.repo.StoreMapsRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ShortenerAccessSvcMapImpl implements ShortenerAccessSvc {

    private Map<String,String> shortToLongMap;
    private Map<String,String> longToShortMap;
    private ShortenerSvc shortenerSvc;
    private StoreMapsRepo storeMapsToFilesSvc;

    @Value("${app.config.hostname}")
    private String hostname;

    public ShortenerAccessSvcMapImpl(Map<String, String> shortToLongMap, Map<String, String> longToShortMap, ShortenerSvc shortenerSvc, StoreMapsRepo storeMapsToFilesSvc) {
        this.shortToLongMap = shortToLongMap;
        this.longToShortMap = longToShortMap;
        this.shortenerSvc = shortenerSvc;
        this.storeMapsToFilesSvc = storeMapsToFilesSvc;
    }

    @Override
    public ShortUrlResponseDto createShortUrl(ShortUrlRequestDto shortUrlRequestDto) throws IOException, URISyntaxException {
        String longUrl = shortUrlRequestDto.getLongUrl();
        String shortUrlKey = getShortUrl(longUrl);
        if(shortUrlKey==null){
            shortUrlKey = shortenerSvc.getShortUrlKey(longUrl);
            shortToLongMap.put(shortUrlKey, longUrl);
            longToShortMap.put(longUrl, shortUrlKey);
            storeMapsToFilesSvc.writeMapsToFiles();
        }
        return getShortUrlResponseDto(shortUrlRequestDto,shortUrlKey);
    }

    @Override
    public String getLongUrl(String shortUrlKey) {
        return shortToLongMap.get(shortUrlKey);
    }

    @Override
    public String getShortUrl(String longUrl) {
        return longToShortMap.get(longUrl);
    }

    ShortUrlResponseDto getShortUrlResponseDto(ShortUrlRequestDto requestDto, String shortUrlKey){
        ShortUrlResponseDto responseDto = new ShortUrlResponseDto();
        responseDto.setLongUrl(requestDto.getLongUrl());
        responseDto.setShortUrl(String.format("%s/%s",hostname,shortUrlKey));
        return responseDto;
    }
}
