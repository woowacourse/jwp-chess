package chess.controller;

import chess.domain.piece.Color;
import chess.dto.ExceptionResponse;
import chess.dto.GameCreateRequest;
import chess.dto.GameCreateResponse;
import chess.dto.GameDeleteRequest;
import chess.dto.GameDeleteResponse;
import chess.dto.GameDto;
import chess.dto.MoveRequest;
import chess.dto.MoveResponse;
import chess.dto.PositionDto;
import chess.exception.DeleteFailOnPlayingException;
import chess.exception.PasswordNotMatchedException;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<GameCreateResponse> create(@RequestBody GameCreateRequest gameCreateRequest) {
        return new ResponseEntity<>(chessService.create(gameCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GameDto>> findAll() {
        return new ResponseEntity<>(chessService.findAll(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDto> findById(@PathVariable int id) {
        return new ResponseEntity<>(chessService.findById(id), HttpStatus.FOUND);
    }

    @GetMapping("/{id}/score")
    public Map<Color, Double> findScoreById(@PathVariable int id) {
        return chessService.findScoreById(id);
    }

    @GetMapping("/{id}/board")
    public List<PositionDto> findPositionsById(@PathVariable int id) {
        return chessService.findPositionsById(id);
    }

    @PatchMapping("/{id}/board")
    public MoveResponse updateBoard(@PathVariable long id, @RequestBody MoveRequest moveRequest) {
        return chessService.updateBoard(id, moveRequest);
    }

    @DeleteMapping
    public ResponseEntity<GameDeleteResponse> deleteById(@RequestBody GameDeleteRequest gameDeleteRequest) {
        return new ResponseEntity<>(chessService.deleteById(gameDeleteRequest), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> unknownExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class,
            DeleteFailOnPlayingException.class})
    public ResponseEntity<ExceptionResponse> knownExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PasswordNotMatchedException.class})
    public ResponseEntity<ExceptionResponse> unauthorizedExceptionHandler(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
