package com.mika.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@RestController
public class SampleController {
    final WebClient webClient;

    public SampleController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/sample")
    Mono<Map<String, String>> getSample() {
        return webClient.get().uri(URI.create("http://localhost:8081/ping"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Ping.class)
                .map(ping -> Map.of("sample", ping.ping));
    }

    public static class Ping {
        public String ping;
    }
}
