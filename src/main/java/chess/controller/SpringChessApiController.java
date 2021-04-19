package chess.controller;

import chess.domain.Game;
import chess.dto.GameResponseDto;
import chess.dto.MovedInfoDto;
import chess.dto.StatusDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessApiController {

    private final ChessService chessService;

    public SpringChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/game")
    public GameResponseDto game(@RequestParam String roomName) {
        Game currentGame = chessService.currentGame(roomName);
        return GameResponseDto.of(currentGame);
    }

    @PostMapping("/restart")
    public GameResponseDto restart(@RequestParam String roomName) {
        Game currentGame = chessService.currentGame(roomName);
        currentGame.init();
        return GameResponseDto.of(currentGame);

    }

    @PostMapping("/move")
    public MovedInfoDto move(@RequestParam String roomName, @RequestParam String source, @RequestParam String target) {
        Game currentGame = chessService.currentGame(roomName);
        currentGame.move(source, target);
        if (currentGame.isEnd()) {
            chessService.deleteRoom(roomName);
            return new MovedInfoDto(source, target, currentGame.winnerColor().getSymbol());
        }

        return new MovedInfoDto(source, target, currentGame.turnColor().getName());
    }

    @PostMapping("/status")
    public StatusDto status(@RequestParam String roomName) {
        Game currentGame = chessService.currentGame(roomName);
        return new StatusDto(currentGame);
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
