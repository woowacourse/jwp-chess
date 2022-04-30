package chess.web;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringBootTestConfig {

    @LocalServerPort
    int port;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

}
