package chess.web.controller;

import chess.domain.state.StateType;
import chess.web.dto.DeleteGameRequestDto;
import chess.web.dto.MovePositionsDto;
import chess.web.dto.MoveResultDto;
import chess.web.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public String showIndex(final Model model) {
        model.addAttribute("games", chessService.getAllGame());
        return "index";
    }

    @PostMapping("/")
    public String createGame(@RequestParam String title, @RequestParam String password) {
        int gameId = chessService.newGame(title, password);
        return "redirect:/game/" + gameId;
    }

    @PostMapping("/delete")
    public String deleteGame(@RequestBody DeleteGameRequestDto deleteGameRequestDto) {
        chessService.deleteGame(deleteGameRequestDto);
        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @GetMapping("/game/{gameId}")
    public String showGame(@PathVariable int gameId, final Model model) {
        if (chessService.getStateType(gameId) == StateType.END) {
            return "redirect:/game/" + gameId + "/result";
        }
        model.addAttribute("chessStatus", chessService.getChessStatus(gameId));

        return "game";
    }

    @PatchMapping("/game/{gameId}/move")
    public ResponseEntity<MoveResultDto> move(@PathVariable int gameId,
                                              @RequestBody MovePositionsDto movePositionsDto) {
        return ResponseEntity.ok(chessService.getMoveResult(gameId, movePositionsDto));
    }

    @GetMapping("/game/{gameId}/result")
    public String showResult(@PathVariable int gameId, final Model model) {
        model.addAttribute("result", chessService.getChessResult(gameId));
        return "result";
    }

    @GetMapping("/game/{gameId}/restart")
    public String restartGame(@PathVariable int gameId) {
        chessService.start(gameId);
        return "redirect:/game/" + gameId;
    }
}