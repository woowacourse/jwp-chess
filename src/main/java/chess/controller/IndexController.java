package chess.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String mainScreen() {
        return "main.html";
    }

    @GetMapping(value = "/room/{name}")
    public String chessScreen() {
        return "/index.html";
    }
}
