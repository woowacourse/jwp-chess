package chess;

import chess.domain.Rooms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringChessApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }

    @Bean
    public Rooms rooms() {
        return new Rooms();
    }
}
