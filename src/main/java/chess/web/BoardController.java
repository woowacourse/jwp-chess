package chess.web;

import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.MoveRequest;
import chess.dto.PathDto;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("rooms/{id}/board")
public class BoardController {

    private final ChessService service;

    @GetMapping(value = {"/init"})
    public ResponseEntity<BoardDto> getNewBoard(@PathVariable int id) {
        return ResponseEntity.ok(new BoardDto(service.restartBoardById(id)));
    }

    @PostMapping("/restart")
    public ResponseEntity<BoardDto> restart(@PathVariable int id) {
        return ResponseEntity.ok(new BoardDto(service.restartBoardById(id)));
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> isEnd(@PathVariable int id) {
        return ResponseEntity.ok(service.endGameById(id));
    }

    @GetMapping("/turn")
    public ResponseEntity<PieceColor> getTurn(@PathVariable int id) {
        return ResponseEntity.ok(service.findTurnById(id));
    }

    @GetMapping("/score")
    public ResponseEntity<Map<PieceColor, Double>> getScore(@PathVariable int id) {
        return ResponseEntity.ok(service.getScoresById(id));
    }

    @PostMapping(path = "/path")
    public ResponseEntity<List<String>> movablePath(@RequestBody PathDto dto,
        @PathVariable int id) {
        return ResponseEntity.ok(service.findPathById(Position.of(dto.getFrom()), id));
    }

    @PostMapping(path = "/move")
    public ResponseEntity<Boolean> move(@RequestBody MoveRequest dto, @PathVariable int id) {
        return ResponseEntity
            .ok(service.addMoveById(Position.of(dto.getFrom()), Position.of(dto.getTo()), id));
    }
}
