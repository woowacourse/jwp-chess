package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessViewController {

    @GetMapping
    public String showRooms() {
        return "rooms";
    }

    @GetMapping("/chess-game/{id}")
    public String showChessGame() {
        return "chessGame";
    }
}
