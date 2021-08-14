package com.shorturl.repo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;


public interface StoreMapsRepo {
    void load(String fileName, Map<String, String> map) throws IOException, URISyntaxException;
    void writeMapsToFiles() throws IOException, URISyntaxException;
}
