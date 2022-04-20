package chess;

import chess.domain.ChessGame;
import chess.domain.position.Square;
import chess.service.DBService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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

    @PostMapping("/ingame/{gameID}")
    public String movePiece(@PathVariable String gameID, @RequestBody String movement, Model model) {
        ChessGame chessGame = DBService.loadSavedChessGame(gameID, DBService.getTurn(gameID));
        List<String> movements = Arrays.asList(movement.split("&"));

        String source = getPosition(movements.get(0));
        String target = getPosition(movements.get(1));

        try {
            chessGame.move(new Square(source), new Square(target));
            DBService.movePiece(gameID, source, target);
            DBService.updateTurn(gameID, chessGame);
            return runGame(gameID, model);
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("gameID", gameID);

        return "ingame";
    }

    private String getPosition(String input) {
        return input.split("=")[1];
    }
}
