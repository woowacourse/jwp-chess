package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.GameStatus;
import chess.exception.ChessGameException;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChessGameController {

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/chess-game")
    public String chessGame(@RequestParam("chess-game-id") int chessGameId, Model model) {
        ChessGameDto chessGameDto = chessGameService.getOrSaveChessGame(chessGameId);
        model.addAttribute("pieces", chessGameService.findPieces(chessGameDto.getId()));
        model.addAttribute("chessGame", chessGameDto);
        return "chess-game";
    }

    @PostMapping("/chess-game/move")
    public String move(@RequestParam("chess-game-id") int chessGameId,
                       @RequestParam String from,
                       @RequestParam String to,
                       RedirectAttributes attributes) {
        ChessGameDto chessGameDto = chessGameService.move(chessGameId, new Movement(from, to));
        if (isGameFinished(chessGameDto)) {
            attributes.addFlashAttribute("isFinished", true);
            attributes.addFlashAttribute("winner", chessGameDto.getWinner());
        }
        return "redirect:/chess-game?chess-game-id=" + chessGameId;
    }

    private boolean isGameFinished(ChessGameDto chessGameDto) {
        return chessGameDto.getStatus() == GameStatus.FINISHED;
    }

    @ExceptionHandler(ChessGameException.class)
    public String exceptionHandler(ChessGameException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("hasError", true);
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/chess-game?chess-game-id=" + e.getChessGameId();
    }
}
