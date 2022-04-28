package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.request.DeleteGameRequest;
import chess.service.dto.request.MoveRequest;
import chess.service.dto.response.BoardDto;
import chess.service.dto.response.DeleteGameResponse;
import chess.service.dto.response.EndGameResponse;
import chess.service.dto.response.ExceptionResponse;
import chess.service.dto.response.GameResultDto;
import chess.service.dto.response.MoveResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {
    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PutMapping("/new-board/{gameId}")
    public ResponseEntity<Object> initBoard(@PathVariable int gameId) {
        chessService.initGame(gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/board/{gameId}")
    public ResponseEntity<BoardDto> getBoardPieces(@PathVariable int gameId) {
        return ResponseEntity.ok(chessService.getBoard(gameId));
    }

    @PostMapping("/move/{gameId}")
    public ResponseEntity<Object> requestMove(@PathVariable int gameId, MoveRequest moveRequest) {
        MoveResponse moveResponse = chessService.move(gameId, moveRequest.getFrom(), moveRequest.getTo());
        return new ResponseEntity<>(moveResponse, HttpStatus.OK);
    }

    @PutMapping("/game-end/{gameId}")
    public ResponseEntity<EndGameResponse> requestEndGame(@PathVariable int gameId) {
        EndGameResponse endGameResponse = chessService.endGame(gameId);
        return new ResponseEntity<>(endGameResponse, HttpStatus.OK);
    }

    @DeleteMapping("/game")
    public ResponseEntity<DeleteGameResponse> deleteGame(@RequestBody DeleteGameRequest deleteRequest) {
        int id = deleteRequest.getId();
        String password = deleteRequest.getPassword();
        DeleteGameResponse deleteResponse = chessService.deleteGame(id, password);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }

    @GetMapping("/status/{gameId}")
    public ResponseEntity<GameResultDto> renderStatus(@PathVariable int gameId) {
        GameResultDto status = chessService.getResult(gameId);
        chessService.endGame(gameId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
