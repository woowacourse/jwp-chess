package chess.controller;

import chess.dto.ErrorMessageDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.dto.ScoreDto;
import chess.service.SpringChessService;
import chess.view.ChessMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SpringChessGameController {

    private final SpringChessService springChessService;

    public SpringChessGameController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index", HttpStatus.OK);
    }

    @GetMapping("/start/{id}")
    public ResponseEntity<ChessMap> start(@PathVariable long id) {
        return ResponseEntity.ok(springChessService.initializeGame(id));
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<ChessMap> load(@PathVariable long id) {
        return ResponseEntity.ok(springChessService.load(id));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ScoreDto> status(@PathVariable long id) {
        return ResponseEntity.ok(springChessService.getStatus(id));
    }

    @PostMapping("/move/{id}")
    public ResponseEntity<ChessMap> move(@PathVariable long id, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok(springChessService.move(id, moveDto));
    }

    @GetMapping("/end/{id}")
    public ResponseEntity<ResultDto> end(@PathVariable long id) {
        final ResultDto resultDto = springChessService.getResult(id);
        springChessService.initializeGame(id);
        return ResponseEntity.ok(resultDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageDto> handle(Exception e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return ResponseEntity.internalServerError().body(errorMessageDto);
    }
}
