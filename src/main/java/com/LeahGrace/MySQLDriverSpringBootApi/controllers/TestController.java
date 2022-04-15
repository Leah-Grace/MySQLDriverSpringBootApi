package com.LeahGrace.MySQLDriverSpringBootApi.controllers;

import com.LeahGrace.MySQLDriverSpringBootApi.payloads.api.response.Article;
import com.LeahGrace.MySQLDriverSpringBootApi.payloads.api.response.NewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/test")
public class TestController {

    @Value("${LeahGrace.MySQLDriverSpringBootApi.newsApiKey}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;  //Interfaces with external RESTapi's

    @GetMapping("/all")
    public String allAccess() {
        return "public content"; //disregards role
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess() {
        return "User content";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String modAccess() {
        return "Moderator content";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin content";
    }

    @GetMapping("/news/{q}")
    public ResponseEntity<?> getNewsArticles(@PathVariable String q) {
        String uri = "https://newsapi.org/v2/everything?sortBy=popularity&apiKey=" + apiKey + "&q=" + q;

        NewsResponse response = restTemplate.getForObject(uri,NewsResponse.class);

        return ResponseEntity.ok(response.getArticles());


    }
}

