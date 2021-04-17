package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;

@SpringBootApplication
public class SpringChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }
}
