package wooteco.chess.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
