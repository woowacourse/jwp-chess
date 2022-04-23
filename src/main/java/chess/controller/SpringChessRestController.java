package chess.controller;

import chess.domain.piece.Color;
import chess.dto.MoveRequestDto;
import chess.dto.MoveResultDto;
import chess.dto.PositionDto;
import chess.service.ChessService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessRestController {

    private final ChessService chessService;

    public SpringChessRestController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board/{id}")
    public List<PositionDto> board(@PathVariable("id") String id) {
        return chessService.getBoardByGameId(id);
    }

    @PostMapping("/move")
    public MoveResultDto move(@RequestBody MoveRequestDto moveRequestDto) {
        return chessService.move(moveRequestDto);
    }

    @GetMapping("/score/{id}")
    public Map<Color, Double> score(@PathVariable("id") String id) {
        return chessService.getScore(id);
    }

    @GetMapping("/isFinished/{id}")
    public boolean isFinished(@PathVariable("id") String id) {
        return chessService.isFinished(id);
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> exception(Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        map.put("message", e.getMessage());
        return map;
    }
}
