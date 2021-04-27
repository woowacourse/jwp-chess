package chess.controller;

import chess.domain.exceptions.ChessException;
import chess.dto.GameResponseDto;
import chess.dto.MoveRequestDto;
import chess.dto.MoveResponseDto;
import chess.dto.StatusDto;
import chess.service.ChessService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestControllerAdvice
public class SpringChessApiController {

    private final ChessService chessService;

    public SpringChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/game")
    public GameResponseDto game(HttpSession session, @RequestParam String roomName) {
        String password = (String) session.getAttribute("password");
        chessService.setBlackPassword(roomName, password);
        return GameResponseDto.of(chessService.currentGame(roomName, password));
    }

    @GetMapping("/new-game/{name}")
    public GameResponseDto restart(HttpSession session, @PathVariable("name") String roomName) {
        String password = (String) session.getAttribute("password");
        return GameResponseDto.of(chessService.restartGame(roomName, password));
    }

    @PutMapping("/game")
    public MoveResponseDto move(HttpSession session, @RequestBody MoveRequestDto moveRequestDto) {
        String password = (String) session.getAttribute("password");
        return chessService.move(moveRequestDto, password);
    }

    @GetMapping("/status/{name}")
    public StatusDto status(HttpSession session, @PathVariable("name") String roomName) {
        String password = (String) session.getAttribute("password");
        return new StatusDto(chessService.currentGame(roomName, password));
    }

    @DeleteMapping("/game")
    public void end(@RequestParam String roomName) {
        chessService.deleteRoom(roomName);
    }

    @ExceptionHandler({ChessException.class})
    public ResponseEntity<String> error(ChessException e) {
        return new ResponseEntity(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity error(DataAccessException e) {
        e.printStackTrace();
        e.notify();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
