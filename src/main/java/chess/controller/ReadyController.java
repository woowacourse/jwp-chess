package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReadyController {
    
    @GetMapping("/")
    public String index() {
        return "ready";
    }
}
