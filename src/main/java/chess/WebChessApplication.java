package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class WebChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebChessApplication.class, args);
    }
}