package chess.controller;

import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
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
        model.addAllAttributes(chessService.getEmojis(gameCode));

        GameResult gameResult = chessService.getGameResult(gameCode);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        if (chessService.isFinished(gameCode)) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }

        model.addAttribute("turn", chessService.getTurn(gameCode));
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameCode", gameCode);
        return "ingame";
    }

    @PostMapping("/{gameCode}")
    public String movePiece(@PathVariable String gameCode, @RequestBody String movement, Model model) {
        ChessGame chessGame = chessService.loadSavedChessGame(gameCode);

        List<String> movements = Arrays.asList(movement.split("&"));
        String source = getPosition(movements.get(0));
        String target = getPosition(movements.get(1));

        doCastlingOrMove(gameCode, chessGame, source, target, model);

        model.addAllAttributes(chessGame.getEmojis());
        model.addAttribute("gameCode", gameCode);

        GameResult gameResult = chessService.getGameResult(gameCode);
        model.addAttribute("turn", chessService.getTurn(gameCode));
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));

        if (chessGame.isKingDie()) {
            model.addAttribute("msg", "킹 잡았다!! 게임 끝~!~!");
            return "finished";
        }
        return "ingame";
    }

    private void doCastlingOrMove(String gameCode, ChessGame chessGame, String source, String target, Model model) {
        if (chessGame.isCastable(new Square(source), new Square(target))) {
            doCastling(gameCode, chessGame, source, target, model);
            return;
        }
        doMove(gameCode, chessGame, source, target, model);
    }

    private void doCastling(String gameCode, ChessGame chessGame, String source, String target, Model model) {
        chessService.doCastling(gameCode, chessGame, source, target);
        model.addAttribute("msg", "누가 이기나 보자구~!");
    }

    private void doMove(String gameCode, ChessGame chessGame, String source, String target, Model model) {
        try {
            chessService.move(gameCode, chessGame, source, target);
            model.addAttribute("msg", "누가 이기나 보자구~!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
        }
    }

    private String getPosition(String input) {
        return input.split("=")[1];
    }

    @GetMapping("/restart")
    public String restartGame(@RequestParam String gameCode, Model model) {
        model.addAllAttributes(chessService.getEmojis(gameCode));
        GameResult gameResult = chessService.getGameResult(gameCode);

        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));
        model.addAttribute("turn", chessService.getTurn(gameCode));
        model.addAttribute("msg", "누가 이기나 보자구~!");
        model.addAttribute("gameCode", gameCode);

        return "redirect:/ingame?gameCode=" + gameCode;
    }
}
