package chess.controller;

import chess.controller.dto.GameStatusDto;
import chess.controller.dto.MoveDto;
import chess.exception.ChessException;
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
        return "lobby";
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
    private ResponseEntity<GameStatusDto> move(@PathVariable("gameId") Long gameId, @RequestBody MoveDto moveDto) {
        chessService.move(gameId, moveDto);
        return ResponseEntity.ok()
                             .body(chessService.load(gameId));
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> handle(ChessException e) {
        return ResponseEntity.status(e.code())
                             .body(e.desc());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handle(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(e.getMessage());
    }
}
