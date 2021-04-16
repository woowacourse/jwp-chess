package chess.controller;

import chess.dto.RoomIdDTO;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LobbyController {
    private final ChessService chessService;

    @Autowired
    public LobbyController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String lobby() {
        return "lobby";
    }

    @GetMapping("/lobby/new")
    @ResponseBody
    public RoomIdDTO newGame() {
        return chessService.newGame();
    }
}
