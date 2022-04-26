package chess.controller;

import chess.domain.piece.Color;
import chess.dto.ExceptionDto;
import chess.dto.MoveRequestDto;
import chess.dto.MoveResultDto;
import chess.dto.NewGameRequest;
import chess.dto.NewGameResponse;
import chess.dto.PositionDto;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/board")
    public ResponseEntity<NewGameResponse> createBoard(@RequestBody NewGameRequest newGameRequest) {
        return new ResponseEntity<>(chessService.createNewGame(newGameRequest), HttpStatus.CREATED);
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
    public ResponseEntity<ExceptionDto> exception(Exception e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
