package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChesViewController {

    private final ChessService chessService;

    public ChesViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String main() {
        return "/main.html";
    }

    @GetMapping("/rooms/{roomId}")
    public String room(@PathVariable Long roomId) {
        chessService.validateExistRoom(roomId);
        return "/room.html";
    }
}
