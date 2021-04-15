package chess.controller;

import chess.domain.Game;
import chess.domain.board.Board;
import chess.dto.GameResponseDto;
import chess.dto.SquareDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpringChessApiController {

    private final ChessService chessService;

    public SpringChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/game")
    public GameResponseDto game(@RequestParam String roomName) {
        Game currentGame = chessService.currentGame(roomName);
        return new GameResponseDto(squareDtos(currentGame.getBoard()),
            currentGame.turnColor().getName());
    }

    @PostMapping("/restart")
    public GameResponseDto restart(@RequestParam String roomName) {
        Game currentGame = chessService.currentGame(roomName);
        currentGame.init();
        return new GameResponseDto(squareDtos(currentGame.getBoard()),
            currentGame.turnColor().getName());
    }

    private List<SquareDto> squareDtos(Board board) {
        List<SquareDto> squareDtos = new ArrayList<>();
        board.positions()
            .forEach(key ->
                squareDtos.add(new SquareDto(key.toString(), board.pieceAtPosition(key).toString()))
            );

        return squareDtos;
    }

    @PostMapping("/move")
    public String move(@RequestParam String roomName, @RequestParam String source, @RequestParam String target) {
        Game currentGame = chessService.currentGame(roomName);
        currentGame.move(source, target);
        if (currentGame.isEnd()) {
            chessService.deleteRoom(roomName);
            return source + " " +
                target + " " + currentGame.winnerColor().getSymbol();
        }

        return source + " " +
            target + " " + currentGame.turnColor().getName();
    }

    @PostMapping("/status")
    public String status(@RequestParam String roomName) {
        Game currentGame = chessService.currentGame(roomName);
        return currentGame.computeWhitePoint() + " " + currentGame.computeBlackPoint();
    }

    @PostMapping("/end")
    public void end(@RequestParam String roomName) {
        Game currentGame = chessService.currentGame(roomName);
        chessService.savePlayingBoard(roomName,
            currentGame.getBoard(),
            currentGame.turnColor()
        );
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> error(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
