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
@RequestMapping("/ingame")
public class InGameController {
    private final ChessService chessService;

    public InGameController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping()
    public String runGame(@RequestParam String gameCode, Model model) {
        final String gameID = chessService.findGameID(gameCode);
        ChessGame chessGame = chessService.loadGame(gameID);
        chessService.loadPieces(gameID);
        GameResult gameResult = chessService.getGameResult(gameID);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameCode", gameCode);
        return "ingame";
    }

    @PostMapping("/{gameCode}")
    public String movePiece(@PathVariable String gameCode, @RequestBody String movement, Model model) {
        final String gameID = chessService.findGameID(gameCode);
        ChessGame chessGame = chessService.loadSavedChessGame(gameID, chessService.getTurn(gameID));
        List<String> movements = Arrays.asList(movement.split("&"));

        String source = getPosition(movements.get(0));
        String target = getPosition(movements.get(1));

        try {
            chessGame.move(new Square(source), new Square(target));
            chessService.movePiece(gameID, source, target);
            chessService.updateTurn(gameID, chessGame);
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAllAttributes(chessGame.getEmojis());
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("gameCode", gameCode);
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
