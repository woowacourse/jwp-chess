package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
@Controller
public class SpringChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "ready";
    }
//
//    @GetMapping("/findgame")
//    public String askGameID() {
//        return "findgame";
//    }
//
//    @PostMapping("/findgame")
//    public String findGame() {
//        return "findgame";
//    }



    @GetMapping("/status")
    public String showResult() {
        return "status";
    }
}
