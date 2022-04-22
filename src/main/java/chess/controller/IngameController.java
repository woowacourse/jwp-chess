package chess.controller;

import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.piece.Color;
import chess.domain.position.Square;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class IngameController {
    private final ChessService chessService;

    public IngameController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping(value = "/ingame", params = "gameID")
    public String runGame(@RequestParam String gameID, Model model) {
        ChessGame chessGame = DBService.loadGame(gameID);
        DBService.startGame(gameID, chessGame);
        DBService.loadPieces(gameID);
        GameResult gameResult = DBService.getGameResult(gameID);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameID", gameID);
        return "ingame";
    }

    @PostMapping("/ingame/{gameID}")
    public String movePiece(@PathVariable String gameID, @RequestBody String movement, Model model) {
        ChessGame chessGame = chessService.loadSavedChessGame(gameID, chessService.getTurn(gameID));
        List<String> movements = Arrays.asList(movement.split("&"));

        String source = getPosition(movements.get(0));
        String target = getPosition(movements.get(1));

        try {
            chessGame.move(new Square(source), new Square(target));
            chessService.movePiece(gameID, source, target);
            chessService.updateTurn(gameID, chessGame);
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg","누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("gameID", gameID);
        GameResult gameResult = chessService.getGameResult(gameID);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        if (chessGame.isKingDie()) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        return "ingame";
    }

    private String getPosition(String input) {
        return input.split("=")[1];
    }
}
