package chess.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessController {
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String welcomeAsHTML() {
        return "index.html";
    }
}
