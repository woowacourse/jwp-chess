package chess.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.domain.Color;
import chess.domain.game.ChessGame;
import chess.dto.GameDto;
import chess.dto.PlayerScoresDto;
import chess.service.GameService;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/start")
    public String createNewGame() {
        final ChessGame chessGame = gameService.createNewGame();
        return "redirect:/games/" + chessGame.getId();
    }

    @GetMapping("/{gameId}")
    public String loadGame(@PathVariable("gameId") final Long gameId, final Model model) {
        return renderBoard(gameService.loadGame(gameId), model);
    }

    @PostMapping("/{gameId}/move")
    public String movePiece(@PathVariable("gameId") final Long gameId,
                            @RequestParam("source") final String source,
                            @RequestParam("target") final String target,
                            final Model model) {
        return renderBoard(gameService.movePiece(gameId, source, target), model);
    }

    @PostMapping("/{gameId}/promotion")
    public String promotion(@PathVariable("gameId") final Long gameId,
                            @RequestParam("pieceName") final String pieceName,
                            final Model model) {
        return renderBoard(gameService.promotion(gameId, pieceName), model);
    }

    @GetMapping("/{gameId}/status")
    public ResponseEntity<PlayerScoresDto> calculatePlayerScores(@PathVariable("gameId") final Long gameId) {
        final Map<Color, Double> playerScores = gameService.calculatePlayerScores(gameId);
        return ResponseEntity.ok().body(PlayerScoresDto.toDto(playerScores));
    }

    @GetMapping("/{gameId}/end")
    public String endGame(@PathVariable("gameId") final Long gameId, final Model model) {
        return renderBoard(gameService.endGame(gameId), model);
    }

    private String renderBoard(final ChessGame chessGame, final Model model) {
        model.addAttribute("game", GameDto.toDto(chessGame));
        if (chessGame.isRunning()) {
            model.addAttribute("promotable", chessGame.isPromotable());
        }
        return "board";
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(final RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
