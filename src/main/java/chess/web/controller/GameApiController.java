package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.request.MoveRequest;
import chess.service.dto.response.BoardDto;
import chess.service.dto.response.EndGameResponse;
import chess.service.dto.response.GameResultDto;
import chess.service.dto.response.MoveResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("room/{roomId}/game")
public class GameApiController {

    private final ChessService chessService;

    public GameApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/board")
    public ResponseEntity<Object> initBoard(@PathVariable Integer roomId) {
        chessService.initGame(roomId);
        URI uri;
        try {
            uri = new URI("/board/" + roomId);
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("URI 형식이 잘못됐습니다.");
        }
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/board")
    public BoardDto getBoardPieces(@PathVariable Integer roomId) {
        return chessService.getBoard(roomId);
    }

    @PatchMapping("/board")
    public MoveResponse move(@PathVariable Integer roomId, MoveRequest moveRequest) {
        return chessService.move(roomId, moveRequest.getFrom(), moveRequest.getTo());
    }

    @PutMapping("/game-end")
    public EndGameResponse endGame(@PathVariable Integer roomId) {
        return chessService.endGame(roomId);
    }

    @GetMapping("/status")
    public GameResultDto getResult(@PathVariable Integer roomId) {
        GameResultDto status = chessService.getResult(roomId);
        chessService.endGame(roomId);
        return status;
    }
}
