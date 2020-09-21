package com.mika.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    // Client config
    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-webclient-runtime
    // and
    // https://docs.spring.io/spring/docs/5.2.9.RELEASE/spring-framework-reference/web-reactive.html#webflux-client-builder
    // and
    // https://netty.io/4.0/api/io/netty/handler/timeout/ReadTimeoutHandler.html
    @Bean
    public WebClient sampleClient() {
        var httpClient = HttpClient.create()
                .tcpConfiguration(client -> client
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
                        .doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(3))
                                .addHandlerLast(new WriteTimeoutHandler(3))));
        var connector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder().clientConnector(connector).build();
    }
}
