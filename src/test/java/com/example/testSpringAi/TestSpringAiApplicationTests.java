package com.example.testSpringAi;

import com.example.testSpringAi.model.Answer;
import com.example.testSpringAi.model.Question;
import com.example.testSpringAi.service.BoardGameService;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(properties = {
        "spring.ai.openai.api-key=test-api-key",
        "spring.ai.openai.base-url=http://localhost:18089/v1",
        "spring.ai.openai.chat.options.model=gpt-5.6-luna"
})
class TestSpringAiApplicationTests {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().port(18089))
            .build();

    @Autowired
    BoardGameService boardGameService;

    @Test
    void contextLoads() {
    }

    @Test
    void asksQuestionAndReturnsDeterministicAnswer() {
        wireMock.stubFor(post(urlPathEqualTo("/v1/chat/completions"))
                .withRequestBody(matchingJsonPath("$.model", equalTo("gpt-5.6-luna")))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "id":"chatcmpl-test",
                                  "object":"chat.completion",
                                  "created":1677652288,
                                  "model":"gpt-5.6-luna",
                                  "choices":[{
                                    "index":0,
                                    "message":{"role":"assistant","content":"A board game is won by following its objective and outscoring or eliminating the opposition."},
                                    "finish_reason":"stop"
                                  }],
                                  "usage":{"prompt_tokens":10,"completion_tokens":15,"total_tokens":25}
                                }
                                """)));

        Answer answer = boardGameService.askQuestion(
                new Question("What is the goal of a board game?"));

        org.junit.jupiter.api.Assertions.assertEquals(
                "A board game is won by following its objective and outscoring or eliminating the opposition.",
                answer.answer());
        wireMock.verify(postRequestedFor(urlPathEqualTo("/v1/chat/completions")));
    }
}
