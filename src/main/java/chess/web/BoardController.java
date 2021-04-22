package chess.web;

import chess.service.ChessService;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.MoveRequest;
import chess.dto.PathDto;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rooms/{id}/board")
public class BoardController {

    private final ChessService service;

    public BoardController(ChessService service) {
        this.service = service;
    }

    @GetMapping(value = {"/init", "/restart"})
    public BoardDto getNewBoard(@PathVariable int id) {
        return new BoardDto(service.restartBoardById(id));
    }

    @GetMapping("/status")
    public boolean isEnd(@PathVariable int id) {
        return service.endGameById(id);
    }

    @GetMapping("/turn")
    public PieceColor getTurn(@PathVariable int id) {
        return service.findTurnById(id);
    }

    @GetMapping("/score")
    public Map<PieceColor, Double> getScore(@PathVariable int id) {
        return service.getScoresById(id);
    }

    @PostMapping(path = "/path")
    public List<String> movablePath(@RequestBody PathDto dto, @PathVariable int id) {
        return service.findPathById(Position.of(dto.getFrom()), id);
    }

    @PostMapping(path = "/move")
    public boolean move(@RequestBody MoveRequest dto, @PathVariable int id) {
        return service.addMoveById(Position.of(dto.getFrom()), Position.of(dto.getTo()), id);
    }
}
