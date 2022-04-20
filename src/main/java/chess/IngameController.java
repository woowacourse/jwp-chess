package chess;

import chess.domain.ChessGame;
import chess.domain.position.Square;
import chess.service.DBService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IngameController {
    private final DBService DBService;

    public IngameController() {
        this.DBService = new DBService();
    }

    @GetMapping(value = "/ingame", params = "gameID")
    public String runGame(@RequestParam String gameID, Model model) {
        ChessGame chessGame = DBService.loadGame(gameID);
        DBService.startGame(gameID, chessGame);
        DBService.loadPieces(gameID);

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameID", gameID);
        return "ingame";
    }
}
