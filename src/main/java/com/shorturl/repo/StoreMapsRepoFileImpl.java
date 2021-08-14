package com.shorturl.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.springframework.util.ResourceUtils.getFile;

@Repository
public class StoreMapsRepoFileImpl implements StoreMapsRepo {

    private final Map<String, String> shortToLongMap;
    private final Map<String, String> longToShortMap;

    @Value("${app.config.shortToLong}")
    private String shortToLongFilePath;

    @Value("${app.config.longToShort}")
    private String longToShortFilePath;

    private ObjectMapper objectMapper = new ObjectMapper();

    public StoreMapsRepoFileImpl(Map<String, String> shortToLongMap, Map<String, String> longToShortMap) {
        this.shortToLongMap = shortToLongMap;
        this.longToShortMap = longToShortMap;
    }

    @PostConstruct
    public void loadMaps() throws IOException {
        synchronized (shortToLongMap) {
            load(shortToLongFilePath, shortToLongMap);
        }
        synchronized (longToShortMap) {
            load(longToShortFilePath, longToShortMap);
        }
    }

    @Override
    public void load(String fileName, Map<String, String> map) throws IOException {
        File file =getFile(String.format("classpath:%s",fileName));
        if(file.length()<=1){
                    return;
        }
        map = objectMapper
                .readValue(file, new TypeReference<>() {
                });
        if (map != null && map.size() > 0) {
            shortToLongMap.putAll(map);
        }
    }

    @Async
    @Override
    public void writeMapsToFiles() throws IOException {
        write(shortToLongMap,shortToLongFilePath);
        write(longToShortMap,longToShortFilePath);
    }

    private void write(Map<String,String> map, String fileName) throws IOException {
        synchronized (this) {
            objectMapper.writeValue(getFile(String.format("classpath:%s",fileName)), map);
        }
    }
}
