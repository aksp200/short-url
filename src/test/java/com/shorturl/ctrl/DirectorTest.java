package com.shorturl.ctrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shorturl.ShortUrlApplication;
import com.shorturl.config.AppConfig;
import com.shorturl.model.ShortUrlRequestDto;
import com.shorturl.model.ShortUrlResponseDto;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShortUrlApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(
        classes = {AppConfig.class}
)
public class DirectorTest {

    @Autowired
    private Shortener shortener;

    @Autowired
    private Director director;

    @Autowired
    private MockMvc mockMvc;

    private Map<String,String> urlMap = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    private String hostname = "http://sh.rt/";

    @Test
    public void redirectToLongUrl() throws Exception {
        String shortUrlKey = createShortUrlAndGetKey();
        MvcResult mvcResult = mockMvc.perform(get("/"+shortUrlKey))
                .andExpect(status().isFound())
                .andReturn();
        String redirectUrl = mvcResult.getResponse().getRedirectedUrl();
        assertEquals(urlMap.get(shortUrlKey),redirectUrl);
    }

    private String createShortUrlAndGetKey() throws Exception {
        ShortUrlRequestDto shortUrlRequestDto = getShortUrlRequestDto();
        MvcResult mvcResult = mockMvc.perform(post("/short")
        .contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(shortUrlRequestDto)))
        .andExpect(status().isCreated())
        .andReturn();
        ShortUrlResponseDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShortUrlResponseDto.class);
        String shortUrlKey = responseDto.getShortUrl().replace(hostname,"");
        urlMap.put(shortUrlKey,responseDto.getLongUrl());

        return shortUrlKey;
    }

    private ShortUrlRequestDto getShortUrlRequestDto(){
        String url = String.format("https://www.google.com/search?q=%s", RandomString.make(500));
        return new ShortUrlRequestDto().setLongUrl(url);
    }
}