package chess.controller;

import chess.domain.command.MoveCommand;
import chess.service.dto.ChessResponseDto;
import chess.controller.dto.MoveCommandDto;
import chess.service.dto.ScoresDto;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/load/{id}")
    public ResponseEntity<ChessResponseDto> load(@PathVariable int id) {
        return ResponseEntity.ok().body(chessService.getChess(id));
    }

    @PostMapping("/start")
    public ResponseEntity<ChessResponseDto> start() {
        return ResponseEntity.ok().body(chessService.initializeGame());
    }

    @GetMapping("/score/{id}")
    public ResponseEntity<ScoresDto> score(@PathVariable int id) {
        return ResponseEntity.ok().body(chessService.getScore(id));
    }

    @PostMapping(value = "/move/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessResponseDto> move(@PathVariable int id, @RequestBody MoveCommandDto moveCommandDto) {
        MoveCommand moveCommand = moveCommandDto.toEntity();
        return ResponseEntity.ok().body(chessService.movePiece(id, moveCommand));
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<ScoresDto> end(@PathVariable int id) {
        return ResponseEntity.ok().body(chessService.finishGame(id));
    }
}
