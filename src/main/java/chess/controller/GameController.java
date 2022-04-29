package chess.controller;

import chess.domain.command.MoveCommand;
import chess.dto.ChessResponseDto;
import chess.dto.MoveCommandDto;
import chess.dto.ScoresDto;
import chess.serviece.ChessGameService;
import chess.serviece.ChessService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GameController {

    private final ChessService chessService;
    private final ChessGameService chessGameService;

    public GameController(ChessService chessService, ChessGameService chessGameService) {
        this.chessService = chessService;
        this.chessGameService = chessGameService;
    }

    @GetMapping
    public String init() {
        return "game";
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<ChessResponseDto> load() {
        return ResponseEntity.ok().body(chessService.getChess());
    }

    @PutMapping("/start")
    public ResponseEntity<ChessResponseDto> start() {
        return ResponseEntity.ok().body(chessService.initializeGame());
    }

    @GetMapping("/score")
    public ResponseEntity<ScoresDto> score() {
        return ResponseEntity.ok().body(chessService.getScore());
    }

    @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessResponseDto> move(@RequestBody MoveCommandDto moveCommandDto) {
        MoveCommand moveCommand = moveCommandDto.toEntity();
        return ResponseEntity.ok().body(chessService.movePiece(moveCommand));
    }

    @PostMapping("/end")
    public ResponseEntity<ScoresDto> end() {
        return ResponseEntity.ok().body(chessService.finishGame());
    }
}
