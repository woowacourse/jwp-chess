package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChessController {

    @RequestMapping(value = "/")
    public String index() {
        return "index.html";
    }
}
