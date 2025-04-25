package njslcp.project.guess_game.integration;

import njslcp.project.guess_game.config.TestConfig;
import njslcp.project.guess_game.config.TestContainerConfig;
import njslcp.project.guess_game.dto.GameRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestContainerConfig.class,TestConfig.class})
@AutoConfigureMockMvc
class WebIntegrationTests {

    @LocalServerPort
    private Integer port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Disabled
    void apiTest_startNewGame_then200() {

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Content-type", "application/json");
                    request.getHeaders()
                            .add("X-Request-ID", "12345");
                    return execution.execute(request, body);
                }));

        GameRequestDto req = GameRequestDto.builder()
                                    .difficulty("easy")
                                    .player(Arrays.asList("Player_1"))
                                    .build();

        ResponseEntity<?> response = restTemplate
                .postForEntity("http://localhost:" + port + "/game/single"
                        , req
                        , String.class);

        Assertions.assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());

    }

}
