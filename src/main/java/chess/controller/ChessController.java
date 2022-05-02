package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chess.controller.dto.ControllerDtoAssembler;
import chess.controller.dto.response.PlayerScoresResponse;
import chess.service.ChessService;
import chess.service.dto.response.GameResponseDto;
import chess.service.dto.response.PlayerScoresResponseDto;

@Controller
@RequestMapping("/games")
public class ChessController {

    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/start")
    public String createNewGame(@RequestParam("title") final String title,
                                @RequestParam("password") final String password) {
        return "redirect:/games/" + chessService.createNewGame(title, password);
    }

    @DeleteMapping("/{gameId}/remove")
    public String removeGame(@PathVariable("gameId") final Long gameId,
                             @RequestParam("password") final String password) {
        chessService.removeGame(gameId, password);
        return "redirect:/";
    }

    @GetMapping("/{gameId}")
    public String loadGame(@PathVariable("gameId") final Long gameId, final Model model) {
        return renderBoard(chessService.loadGame(gameId), model);
    }

    @PostMapping("/{gameId}/move")
    public String movePiece(@PathVariable("gameId") final Long gameId,
                            @RequestParam("source") final String source,
                            @RequestParam("target") final String target,
                            final Model model) {
        return renderBoard(chessService.movePiece(gameId, source, target), model);
    }

    @PostMapping("/{gameId}/promotion")
    public String promotion(@PathVariable("gameId") final Long gameId,
                            @RequestParam("pieceName") final String pieceName,
                            final Model model) {
        return renderBoard(chessService.promotion(gameId, pieceName), model);
    }

    @GetMapping("/{gameId}/status")
    @ResponseBody
    public PlayerScoresResponse calculatePlayerScores(@PathVariable("gameId") final Long gameId) {
        final PlayerScoresResponseDto playerScoresResponseDto = chessService.calculatePlayerScores(gameId);
        return ControllerDtoAssembler.playerScoresResponse(playerScoresResponseDto);
    }

    @PostMapping("/{gameId}/end")
    public String endGame(@PathVariable("gameId") final Long gameId, final Model model) {
        return renderBoard(chessService.endGame(gameId), model);
    }

    private String renderBoard(final GameResponseDto gameResponseDto, final Model model) {
        model.addAttribute("game", ControllerDtoAssembler.gameResponse(gameResponseDto));
        return "board";
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(final RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
