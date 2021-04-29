package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/rooms/{roomId}")
    public String boardPage(){
        return "board";
    }
}
