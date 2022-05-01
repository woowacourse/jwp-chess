package chess.controller;

import chess.dto.MoveRequest;
import chess.dto.ScoreDto;
import chess.service.ChessService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PatchMapping("/room/{roomId}/move")
    public ResponseEntity<Boolean> move(@RequestBody @Valid MoveRequest moveRequest, @PathVariable int roomId) {
        chessService.move(moveRequest.getSource(), moveRequest.getTarget(), roomId);
        return ResponseEntity.ok().body(chessService.isEnd(roomId));
    }

    @GetMapping("/room/{roomId}/status")
    public ResponseEntity<ScoreDto> status(@PathVariable int roomId) {
        return ResponseEntity.ok().body(ScoreDto.from(chessService.status(roomId)));
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<String> deleteRoom(@RequestBody String password, @PathVariable int roomId) {
        chessService.deleteRoom(roomId, password);
        return ResponseEntity.ok().build();
    }
}
