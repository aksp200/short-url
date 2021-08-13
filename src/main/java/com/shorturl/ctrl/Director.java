package com.shorturl.ctrl;

import com.shorturl.svc.ShortenerAccessSvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class Director {

    ShortenerAccessSvc shortenerAccessSvc;

    public Director(ShortenerAccessSvc shortenerAccessSvc) {
        this.shortenerAccessSvc = shortenerAccessSvc;
    }

    @GetMapping("/{shortUrlKey}")
    public void redirectToLongUrl(@PathVariable String shortUrlKey, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect(shortenerAccessSvc.getLongUrl(shortUrlKey));
    }
}
