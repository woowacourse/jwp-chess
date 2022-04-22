package chess.controller;

import chess.dto.BoardDto;
import chess.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/game")
public class SpringWebChessController {

    @Autowired
    private GameService gameService;

    @GetMapping("")
    public String board() {
        return "board";
    }

    @ResponseBody
    @PostMapping("/start")
    public BoardDto start() {
        return gameService.start();
    }
}
