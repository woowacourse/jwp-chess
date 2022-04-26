package chess.controller;

import chess.dto.MoveDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PlayController {
    private final ChessGameService chessGameService;

    public PlayController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PutMapping(path = "/game/{gameId}/move")
    public ResponseEntity<String> move(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        chessGameService.move(gameId, moveDto);
        return ResponseEntity.ok().body(renderGame(gameId));
    }

    @GetMapping("/game/{gameId}/exit")
    public String exitAndDeleteGame(@PathVariable String gameId) {
        chessGameService.cleanGame(gameId);
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    private String renderGame(String gameId) {
        return "redirect:/game/" + gameId;
    }
}
