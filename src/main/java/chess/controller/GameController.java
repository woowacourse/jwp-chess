package chess.controller;

import chess.domain.Score;
import chess.domain.command.MoveCommand;
import chess.domain.piece.PieceColor;
import chess.controller.response.ChessGameResponse;
import chess.controller.request.MoveRequest;
import chess.controller.response.ScoresResponse;
import chess.serviece.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<ChessGameResponse> load(@PathVariable Long id) {
        return ResponseEntity.ok().body(chessGameService.getChessGame(id));
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<ScoresResponse> score(@PathVariable Long id) {
        Map<PieceColor, Score> scoreByColor = chessGameService.getScore(id);
        return ResponseEntity.ok().body(ScoresResponse.of(scoreByColor));
    }

    @PutMapping(value = "/{id}/pieces")
    public ResponseEntity<ChessGameResponse> move(@PathVariable Long id, @RequestBody MoveRequest moveRequest) {
        MoveCommand moveCommand = moveRequest.toEntity();
        return ResponseEntity.ok().body(chessGameService.movePiece(id, moveCommand));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScoresResponse> end(@PathVariable Long id) {
        Map<PieceColor, Score> scoresByColor = chessGameService.finishGame(id);
        return ResponseEntity.ok().body(ScoresResponse.of(scoresByColor));
    }
}
