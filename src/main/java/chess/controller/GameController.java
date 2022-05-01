package chess.controller;

import chess.domain.command.MoveCommand;
import chess.dto.ChessResponseDto;
import chess.dto.MoveCommandDto;
import chess.dto.ScoresDto;
import chess.serviece.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/games")
public class GameController {

    private final ChessGameService chessGameService;

    public GameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping
    public String init() {
        return "game";
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChessResponseDto> load(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessGameService.getChessGame(id));
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<ScoresDto> score(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessGameService.getScore(id));
    }

    @PutMapping(value = "/{id}/pieces")
    public ResponseEntity<ChessResponseDto> move(@PathVariable Long id, @RequestBody MoveCommandDto moveCommandDto) {
        MoveCommand moveCommand = moveCommandDto.toEntity();
        return ResponseEntity.ok().body(chessGameService.movePiece(id, moveCommand));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScoresDto> end(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessGameService.finishGame(id));
    }
}
