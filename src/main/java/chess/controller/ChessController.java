package chess.controller;

import chess.domain.command.MoveCommand;
import chess.dto.ChessResponseDto;
import chess.dto.MoveCommandDto;
import chess.dto.ScoresDto;
import chess.serviece.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chess-game")
public class ChessController {

    private final ChessService chessService;

    @Autowired
    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/load")
    public ResponseEntity<ChessResponseDto> load() {
        return ResponseEntity.ok().body(chessService.getChess());
    }

    @PostMapping("/start")
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
