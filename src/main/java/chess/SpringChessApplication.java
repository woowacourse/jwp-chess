package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class SpringChessApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }
}
