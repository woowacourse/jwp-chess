package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringWebChessApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringWebChessApplication.class, args);
    }
}
