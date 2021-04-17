package chess.controller.web;

import chess.chessgame.domain.manager.ChessGameManager;
import chess.controller.web.dto.ChessGameResponseDto;
import chess.controller.web.dto.ErrorMessageResponseDto;
import chess.controller.web.dto.MoveRequestDto;
import chess.controller.web.dto.MoveResponseDto;
import chess.controller.web.dto.ScoreResponseDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {
    private final ChessService chessService;

    public GameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/start")
    public ChessGameResponseDto gameStart() {
        return new ChessGameResponseDto(chessService.start());
    }

    @GetMapping("/score/{id}")
    public ScoreResponseDto getScore(@PathVariable long id) {
        return new ScoreResponseDto(chessService.getStatistics(id));
    }

    @GetMapping("/load/{id:[\\d]+}")
    public ChessGameResponseDto loadGame(@PathVariable long id) {
        ChessGameManager load = chessService.load(id);
        return new ChessGameResponseDto(load);
    }

    @PostMapping("/move")
    public MoveResponseDto movePiece(@RequestBody MoveRequestDto moveMessage) {
        chessService.move(moveMessage);
        return new MoveResponseDto(chessService.isEnd(moveMessage.getGameId()), chessService.nextColor(moveMessage.getGameId()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageResponseDto> handle(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ErrorMessageResponseDto(e.getMessage()));
    }
}
