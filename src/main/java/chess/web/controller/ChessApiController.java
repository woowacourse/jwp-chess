package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.request.DeleteGameRequest;
import chess.service.dto.request.MoveRequest;
import chess.service.dto.response.BoardDto;
import chess.service.dto.response.DeleteGameResponse;
import chess.service.dto.response.EndGameResponse;
import chess.service.dto.response.GameResultDto;
import chess.service.dto.response.MoveResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PostMapping("/board/{gameId}")
    public ResponseEntity<Object> initBoard(@PathVariable Integer gameId) {
        chessService.initGame(gameId);
        URI uri;
        try {
            uri = new URI("/board/" + gameId);
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("URI 형식이 잘못됐습니다.");
        }
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/board/{gameId}")
    public BoardDto getBoardPieces(@PathVariable Integer gameId) {
        return chessService.getBoard(gameId);
    }

    @PatchMapping("/board/{gameId}")
    public MoveResponse move(@PathVariable Integer gameId, MoveRequest moveRequest) {
        return chessService.move(gameId, moveRequest.getFrom(), moveRequest.getTo());
    }

    @PutMapping("/game-end/{gameId}")
    public EndGameResponse endGame(@PathVariable Integer gameId) {
        return chessService.endGame(gameId);
    }

    @DeleteMapping("/game")
    public DeleteGameResponse deleteGame(@RequestBody DeleteGameRequest deleteRequest) {
        int id = deleteRequest.getId();
        String password = deleteRequest.getPassword();
        return chessService.deleteGame(id, password);
    }

    @GetMapping("/status/{gameId}")
    public GameResultDto getResult(@PathVariable Integer gameId) {
        GameResultDto status = chessService.getResult(gameId);
        chessService.endGame(gameId);
        return status;
    }
}
