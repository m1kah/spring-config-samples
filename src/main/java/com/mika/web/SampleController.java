package com.mika.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
public class SampleController {
    final WebClient webClient;

    public SampleController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/sample")
    Mono<String> getSample() {
        return webClient.get().uri(URI.create("http://localhost:8081/ping"))
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class);
    }
}
