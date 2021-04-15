package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class SpringChessApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/chess")
    public String chess() {
        return "chess";
    }
}
