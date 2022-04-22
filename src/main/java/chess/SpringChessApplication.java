package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static spark.Spark.staticFileLocation;

@SpringBootApplication
@Controller
public class SpringChessApplication {
    public static void main(String[] args) {
        staticFileLocation("/public");
        SpringApplication.run(SpringChessApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
