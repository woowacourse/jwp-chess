package chess.controller;

import chess.controller.dto.GameDto;
import chess.controller.dto.request.GameAccessRequest;
import chess.controller.dto.request.MoveRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ErrorResponse;
import chess.controller.dto.response.StatusResponse;
import chess.service.ChessService;
import java.net.URI;
import java.net.URISyntaxException;
<<<<<<< HEAD
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
=======
import java.util.NoSuchElementException;
>>>>>>> refactor: 게임 조회 시 게임이 없으면 404 NOT FOUND 반환하도록 변경
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{gameId}")
    public ChessGameResponse loadGame(@PathVariable long gameId) {
        return chessService.loadGame(gameId);
    }

    @PostMapping("/{gameId}")
    public ResponseEntity<ChessGameResponse> createGame(@PathVariable long gameId) throws URISyntaxException {
        return ResponseEntity.created(new URI("/api/games/" + gameId)).body(chessService.createGame(gameId));
    }

    @PutMapping("/{gameId}")
    public ChessGameResponse startOrRestartGame(@PathVariable long gameId) {
        return chessService.startOrRestartGame(gameId);
    }

    @PutMapping("/{gameId}/pieces")
    public ChessGameResponse movePiece(@PathVariable long gameId, @RequestBody MoveRequest moveRequest) {
        return chessService.move(gameId, moveRequest);
    }

    @GetMapping("/{gameId}/status")
    public StatusResponse status(@PathVariable long gameId) {
        return chessService.status(gameId);
    }

    @GetMapping("/{gameId}/end")
    public ChessGameResponse endGame(@PathVariable long gameId) {
        return chessService.end(gameId);
    }

    @GetMapping("")
    public List<GameDto> findAllGames() {
        return chessService.findAllGames();
    }

    @PostMapping("/existed-game/?")
    public ResponseEntity<ErrorResponse> comparePassword(@RequestBody GameAccessRequest game) {
        if (chessService.checkPassword(game.getGameId(), game.getPassword())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("잘못된 비밀번호 입니다."));
    }

    @DeleteMapping("/existed-game/?")
    public ResponseEntity<ErrorResponse> deleteGame(@RequestBody GameAccessRequest game) {
        if (chessService.deleteGameAfterCheckingPassword(game.getGameId(), game.getPassword())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("게임이 끝난 상태가 아니거나 올바른 비밀번호가 아닙니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleDataNotFound() {
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/games/{gameId}")
    public ChessGameResponse startOrRestartGame(@PathVariable long gameId) {
        return chessService.startOrRestartGame(gameId);
    }

    @GetMapping("/restart/{gameId}")
    public ChessGameResponse restartGame(@PathVariable long gameId) {
        return chessService.restartGame(gameId);
    }

    @PostMapping("/move/{gameId}")
    public ChessGameResponse movePiece(@PathVariable long gameId, @RequestBody MoveRequest moveRequest) {
        return chessService.move(gameId, moveRequest);
    }

    @GetMapping("/games/{gameId}/status")
    public StatusResponse status(@PathVariable long gameId) {
        return chessService.status(gameId);
    }

    @GetMapping("/end/{gameId}")
    public ChessGameResponse endGame(@PathVariable long gameId) {
        return chessService.end(gameId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleDataNotFound() {
        return ResponseEntity.notFound().build();
    }
}

