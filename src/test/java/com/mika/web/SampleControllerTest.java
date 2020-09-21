package com.mika.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SampleControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    void okResponse() {
        var mockServer = new WireMockServer(wireMockConfig().port(8081));
        mockServer.start();

        mockServer
                .stubFor(get(urlEqualTo("/ping"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "ping": "pong"
                                }
                                """)));

        RestAssured
            .when()
                .get("/sample")
            .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("sample", equalTo("pong"));

        mockServer.stop();
    }
}
