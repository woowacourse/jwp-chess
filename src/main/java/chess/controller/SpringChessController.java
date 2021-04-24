package chess.controller;

import chess.controller.dto.GameStatusDto;
import chess.exception.ChessException;
import chess.exception.DataNotFoundException;
import chess.service.ChessService;
import chess.service.GameService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SpringChessController {
    private final ChessService chessService;
    private final GameService gameService;

    public SpringChessController(ChessService chessService, GameService gameService) {
        this.chessService = chessService;
        this.gameService = gameService;
    }

    @GetMapping("/")
    private String mainPage(Model model) {
        model.addAttribute("gameList", gameService.load());
        return "index";
    }

    @GetMapping("/games/{gameId}")
    private String renderChessBoard(@PathVariable Long gameId) {
        return "chessboard";
    }

    @GetMapping("/games/{gameId}/load")
    private ResponseEntity<GameStatusDto> loadGame(@PathVariable Long gameId) {
        return ResponseEntity.ok()
                             .body(chessService.load(gameId));
    }

    @PostMapping("/games")
    private String createRoom(@RequestParam("gameName") String roomName) {
        Long gameId = gameService.create(roomName);
        return "redirect:games/" + gameId;
    }

    @DeleteMapping("/games/{gameId}")
    private ResponseEntity deleteRoom(@PathVariable("gameId") Long gameId) {
        gameService.delete(gameId);
        return ResponseEntity.ok()
                             .build();
    }

    @PostMapping("/games/{gameId}/move")
    private ResponseEntity<GameStatusDto> move(@PathVariable("gameId") Long gameId,
                        @RequestParam("from") String from,
                        @RequestParam("to") String to) {
        chessService.move(gameId, from, to);
        return loadGame(gameId);
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handle(ChessException e) {
        return ResponseEntity.status(e.code())
                             .body(e.desc());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handle(DataNotFoundException e) {
        return ResponseEntity.badRequest()
                             .body(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handle(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(e.getMessage());
    }
}
