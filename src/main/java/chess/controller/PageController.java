package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    //todo: 페이지 보여주기
    @GetMapping("/game")
    public String boardPage(){
        return "game";
    }
}
